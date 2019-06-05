package com.ml.util.data;

import org.apache.commons.math4.ml.clustering.CentroidCluster;
import org.apache.commons.math4.ml.clustering.DoublePoint;
import org.apache.commons.math4.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math4.ml.clustering.MultiKMeansPlusPlusClusterer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class KMeansAnalysis {

    private static final String APARTMENT_DATA_FILE = "apartment-data.txt";

    public static void main(String[] args) throws Exception {
        MultiKMeansPlusPlusClusterer<DoublePoint> transformer = new MultiKMeansPlusPlusClusterer<>(
                new KMeansPlusPlusClusterer<>(8, 20), 5);

        List<DoublePoint> points = new ArrayList<>();
        Map<DoublePoint, String> pointMap = new HashMap<>();

        Path path = Paths.get(GenerateApartmentURLs.class.getResource("/" + APARTMENT_DATA_FILE).toURI());
        try (Stream<String> stream = Files.lines(path).skip(1)) {
            stream.forEach(line -> {
                String[] features = line.split(",");
                DoublePoint point = new DoublePoint(new double[] {
                        Double.parseDouble(features[1]),
                        Double.parseDouble(features[2]),
                        Double.parseDouble(features[3]),
                        Double.parseDouble(features[4]),
                        Double.parseDouble(features[7]),
                        Double.parseDouble(features[8]),
                        Double.parseDouble(features[9])
                });
                points.add(point);
                pointMap.put(point, generateIconHTML(features));
            });
        }

        List<CentroidCluster<DoublePoint>> clusters = transformer.cluster(points);

        int i = 0;
        for (CentroidCluster<DoublePoint> cluster : clusters) {
            i++;

            System.out.println(i +" CLUSTER. Size = " + cluster.getPoints().size());
            for (DoublePoint point : cluster.getPoints()) {
                System.out.println(pointMap.get(point)
                        .replaceAll("#iconColor#", Color.getColorName(i) + "Icon")
                );
            }
            System.out.println("\n");
        }
    }

    private static String generateIconHTML(String[] features) {
        return "L.marker([" +
                Double.parseDouble(features[5]) +
                ", " +
                Double.parseDouble(features[6]) +
                "], {icon: #iconColor#}).addTo(mymap).bindPopup(\"" +
                features[0] +
                "\");";
    }

    enum Color {
        BLACK(1),
        BLUE(2),
        GREEN(3),
        GREY(4),
        ORANGE(5),
        RED(6),
        VIOLET(7),
        YELLOW(8);

        private int number;

        Color(int number) {
            this.number = number;
        }

        public static String getColorName(int number) {
            for (Color value : values()) {
                if (number == value.number) {
                    return value.name().toLowerCase();
                }
            }
            throw new RuntimeException("There are no colors with number " + number);
        }
    }
}