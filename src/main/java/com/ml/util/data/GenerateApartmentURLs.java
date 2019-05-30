package com.ml.util.data;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateApartmentURLs {

    private static final int START_PAGE_NUMBER = 0;
    private static final int END_PAGE_NUMBER = 155;

    private static final String BLAGOVIST_PAGE_URL = "https://blagovist.ua/search/apartment/sale/cur_3/kch_2?page=";
    private static final String BLAGOVIST_URL_PATTERN = "https://blagovist.ua/object/[0-9]*";

    private static final int THREADS_NUMBER = 20;
    private static final String APARTMENT_URLS_FILE = "apartment-urls.txt";

    public static void main(String[] args) throws Exception {
        List<Callable<Set<String>>> tasks = new ArrayList<>();
        for (int i = START_PAGE_NUMBER; i<= END_PAGE_NUMBER; i++) {
            final int pageNumber = i;
            tasks.add(() -> extractUrls(Utils.getPageResponse(new URL(BLAGOVIST_PAGE_URL + pageNumber))));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_NUMBER);
        List<Future<Set<String>>> results = executorService.invokeAll(tasks);
        Set<String> urls = new HashSet<>();
        for (Future<Set<String>> result : results) {
            urls.addAll(result.get());
        }

        File file = new File(GenerateApartmentURLs.class.getResource("/" + APARTMENT_URLS_FILE).getFile());
        Files.write(file.toPath(), urls);

        executorService.shutdown();
        if (!executorService.awaitTermination(100, TimeUnit.MICROSECONDS)) {
            System.out.println("Still waiting after 100ms: calling System.exit(0)...");
            System.exit(0);
        }
    }

    private static Set<String> extractUrls(String text) {
        Pattern pattern = Pattern.compile(BLAGOVIST_URL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        Set<String> containedUrls = new HashSet<>();
        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }

        return containedUrls;
    }
}

