package com.ml.util.data;

import com.ml.util.data.pojo.ApartmentAge;
import com.ml.util.data.pojo.ApartmentData;
import com.ml.util.data.pojo.ApartmentMaterial;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateApartmentData {

    private static final int THREADS_NUMBER = 20;

    private static final String APARTMENT_DATA_FILE = "apartment-data.csv";
    private static final String APARTMENT_URLS_FILE = "apartment-urls.csv";

    public static void main(String[] args) throws Exception {
        List<Callable<ApartmentData>> tasks = new ArrayList<>();

        Path path = Paths.get(GenerateApartmentURLs.class.getResource("/" + APARTMENT_URLS_FILE).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(url -> {
                tasks.add(() -> getApartmentData(url, extractData(Utils.getPageResponse(new URL(url)))));
            });
        }

        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_NUMBER);
        List<Future<ApartmentData>> results = executorService.invokeAll(tasks);
        Set<ApartmentData> data = new HashSet<>();
        for (Future<ApartmentData> result : results) {
            System.out.println("...");
            ApartmentData apartmentData = result.get();
            if (apartmentData.isCorrect()) {
                data.add(apartmentData);
            }
        }

        List<String> output = new ArrayList<>(Collections.singletonList("URL,Material,TotalArea," +
                "RoomsNumber,FloorNumberCoeff,Latitude,Longitude,Distance,ApartmentAge,Price"));
        output.addAll(data.stream().map(ApartmentData::toString).collect(Collectors.toList()));
        File file = new File(GenerateApartmentData.class.getResource("/" + APARTMENT_DATA_FILE).getFile());
        Files.write(file.toPath(), output);

        executorService.shutdown();
        if (!executorService.awaitTermination(100, TimeUnit.MICROSECONDS)) {
            System.out.println("Still waiting after 100ms: calling System.exit(0)...");
            System.exit(0);
        }
    }

    private static Map<String, String> extractData(String text) {
        Map<String, String> descriptionMap = parseObjectDescription(text);
        Map<String, String> objectMetadata = parseObjectMetadata(text);
        Map<String, String> mergedMap = Stream.concat(descriptionMap.entrySet().stream(), objectMetadata.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        mergedMap.put("Price_USD", parsePrice(text));
        return mergedMap;
    }

    private static Map<String, String> parseObjectMetadata(String text) {
        Map<String, String> result = new HashMap<>();

        String urlRegex = "<script>var  blagovist[\\s\\S]*<!--—END details-->";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);
        while (urlMatcher.find()) {
            String description = text.substring(urlMatcher.start(0), urlMatcher.end(0));
            String[] lines = description
                    .replaceAll("(</script>)|(<script>)", "")
                    .split("var");
            for (String line : lines) {
                String[] map = line.split("=");
                if (map.length == 2) {
                    result.put(map[0].trim(), map[1].trim().replaceAll("(\')|(;)", ""));
                }
            }
        }
        return result;
    }

    private static String parsePrice(String text) {
        String urlRegex = "<!--—BEGIN price-->[\\s\\S]*<!--—END price-->";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);
        while (urlMatcher.find()) {
            String description = text.substring(urlMatcher.start(0), urlMatcher.end(0));
            urlRegex = "content=\"USD[\\s\\S]*\\$";
            pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
            urlMatcher = pattern.matcher(description);

            while (urlMatcher.find()) {
               String tableData = description.substring(urlMatcher.start(0), urlMatcher.end(0));

                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(tableData);
                while (m.find()) {
                    return m.group(1).replaceAll("USD", "").replaceAll("\\s","");
                }
           }
        }
        throw new RuntimeException("Failed to parse the price!");
    }

    private static Map<String, String> parseObjectDescription(String text) {
        Map<String, String> result = new HashMap<>();

        String urlRegex = "<!--—BEGIN details-->[\\s\\S]*<!--—END details-->";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);
        while (urlMatcher.find()) {
            String description = text.substring(urlMatcher.start(0), urlMatcher.end(0));
            urlRegex = "<li>[\\s\\S]*</li>";
            pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
            urlMatcher = pattern.matcher(description);

            while (urlMatcher.find()) {
                String tableData = description.substring(urlMatcher.start(0), urlMatcher.end(0));

                String[] lines = tableData
                        .replaceAll("(</span>)|(<span>)|(<li>)|(</li>)|(</a>)", "")
                        .replaceAll("<a[^>]* href=\"([^\"]*)\">", "")
                        .split("<em class=\"text-muted\">");
                for (String line : lines) {
                    String[] map = line.split("</em>");
                    if (map.length == 2) {
                        result.put(map[0].trim(), map[1].trim());
                    }
                }
            }
        }
        return result;
    }

    private static ApartmentData getApartmentData(String url, Map<String, String> dataMap) {
        ApartmentData.Builder builder = new ApartmentData.Builder(url);
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            String entryKey = entry.getKey();
            String entryValue = entry.getValue();

            if (entryKey.equals("Комнаты")) {
                builder.setRoomsNumber(entryValue.replaceAll("\\D+",""));
            } else if (entryKey.equals("Этаж/Этажей")) {
                builder.setApartmentFloorNumber(entryValue.split("/")[0]);
                builder.setMaxFloorNumber(entryValue.split("/")[1]);
            } else if (entryKey.equals("Материал стен")) {
                builder.setMaterial(ApartmentMaterial.getCode(entryValue));
            } else if (entryKey.startsWith("Площадь (общая")) {
                builder.setTotalArea(entryValue.split("/")[0]);
            } else if (entryKey.equals("Тип (серия) дома")) {
                builder.setApartmentAge(ApartmentAge.getAge(entryValue));
            } else if (entryKey.equals("Price_USD")) {
                builder.setPrice(entryValue);
            } else if (entryKey.equals("blagovist_mod_geo_x")) {
                builder.setLatitude(entryValue);
            } else if (entryKey.equals("blagovist_mod_geo_y")) {
                builder.setLongitude(entryValue);
            }
        }
        return builder.build();
    }
}
