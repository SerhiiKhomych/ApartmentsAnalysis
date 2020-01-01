package com.ml.util.data;

import com.ml.util.data.pojo.ApartmentData;
import com.ml.util.data.pojo.FlatEarthDist;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

public class CSVReader {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Map<String, Integer> resultMap = new HashMap<>();

        Path path = Paths.get(GenerateApartmentURLs.class.getResource("/" + "apartment-data.csv").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(data -> {
                String[] dataArray = data.split(",");
                if (!dataArray[0].equals("URL")) {
                    resultMap.put(dataArray[0], FlatEarthDist.nearSubway(Double.valueOf(dataArray[5]), Double.valueOf(dataArray[6])) ? 1: 0);
                }
            });
        }

        path = Paths.get(GenerateApartmentURLs.class.getResource("/k-means-3/" + "cluster_1_1.csv").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(data -> {
                String[] dataArray = data.split(",");
                if (!dataArray[0].equals("URL")) {
                    System.out.println(data + "," + resultMap.get(dataArray[0]));
                } else {
                    System.out.println("URL,Material,TotalArea,RoomsNumber,FloorNumberCoeff,Distance,ApartmentAge,Price,State,NearSubway");
                }
            });
        }
    }
}
