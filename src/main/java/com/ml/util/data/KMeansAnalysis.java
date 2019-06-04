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
                new KMeansPlusPlusClusterer<>(2, 20), 5);

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
                pointMap.put(point, getGreenIcon(features));
            });
        }

        List<CentroidCluster<DoublePoint>> clusters = transformer.cluster(points);

        int i = 0;
        for (CentroidCluster<DoublePoint> cluster : clusters) {
            System.out.println(cluster.getPoints().size());
            i++;

            System.out.println(i +" CLUSTER");
            for (DoublePoint point : cluster.getPoints()) {
                System.out.println(pointMap.get(point));
            }
            System.out.println("\n\n\n");
        }
    }

    private static String getGreenIcon(String[] features) {
        return "L.marker([" +
                Double.parseDouble(features[5]) +
                ", " +
                Double.parseDouble(features[6]) +
                "], {icon: greenIcon}).addTo(mymap).bindPopup(\"" +
                features[0] +
                "\");";
    }
}