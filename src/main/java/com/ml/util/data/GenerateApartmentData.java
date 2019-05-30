package com.ml.util.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class GenerateApartmentData {

    private static final int THREADS_NUMBER = 20;

    private static final String APARTMENT_DATA_FILE = "apartment-data.txt";
    private static final String APARTMENT_URLS_FILE = "apartment-urls.txt";

    public static void main(String[] args) throws Exception {
        Map<String, Map<String, String>> data = new HashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_NUMBER);

        Path path = Paths.get(GenerateApartmentURLs.class.getResource("/" + APARTMENT_URLS_FILE).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(url -> {
                executorService.execute(() -> {
                    try {
                        data.put(url, extractData(getPageResponse(url)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
            System.out.println("Still waiting after 10 min: calling System.exit(0)...");
            System.exit(0);
        }

        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> stringMapEntry : data.entrySet()) {
            result.add(stringMapEntry.getKey());
            for (Map.Entry<String, String> stringStringEntry : stringMapEntry.getValue().entrySet()) {
                result.add(stringStringEntry.getKey() + "---" + stringStringEntry.getValue());
            }
            result.add("\n");
        }

        File file = new File(GenerateApartmentURLs.class.getResource("/" + APARTMENT_DATA_FILE).getFile());
        Files.write(file.toPath(), result);
    }

    private static String getPageResponse(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static Map<String, String> extractData(String text) {
        Map<String, String> result = new HashMap<>();

        String urlRegex = "<!--—BEGIN details-->[\\s\\S]*'Sale';</script>";
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

        urlRegex = "<script>var  blagovist[\\s\\S]*<!--—END details-->";
        pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        urlMatcher = pattern.matcher(text);
        while (urlMatcher.find()) {
            String description = text.substring(urlMatcher.start(0), urlMatcher.end(0));
            String[] lines = description
                    .replaceAll("(</script>)|(<script>)", "")
                    .split("var");
            for (String line : lines) {
                String[] map = line.split("=");
                if (map.length == 2) {
                    result.put(map[0].trim(), map[1].trim());
                }
            }
        }
        return result;
    }

//    private static ApartmentData getApartmentData(Map<String, String> dataMap) {
//        ApartmentData.Builder builder = new ApartmentData.Builder();
//        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
//            String entryKey = entry.getKey();
//            if (entryKey.equals("Комнаты")) {
//            } else if (entryKey.equals("Этаж/Этажей")) {
//            } else if (entryKey.equals("Материал стен")) {
//            } else if (entryKey.startsWith("Площадь (общая")) {
//            } else if (entryKey.equals("Тип (серия) дома")) {
//            }
//        }
//        return null;
//    }
}
