package com.ml.util.data.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlatEarthDist {

    private final static List<ObjectLocation> subwayStations = new ArrayList<>(Arrays.asList(
            // blue line
            new ObjectLocation(50.522647, 30.498925),
            new ObjectLocation(50.512149, 30.498575),
            new ObjectLocation(50.486132, 30.497840),
            new ObjectLocation(50.474133, 30.503539),
            new ObjectLocation(50.473529, 30.504694),
            new ObjectLocation(50.459285, 30.524387),
            new ObjectLocation(50.458917, 30.524927),
            new ObjectLocation(50.450166, 30.524004),
            new ObjectLocation(50.439611, 30.516826),
            new ObjectLocation(50.432294, 30.516417),
            new ObjectLocation(50.420729, 30.520817),
            new ObjectLocation(50.413240, 30.524430),
            new ObjectLocation(50.393187, 30.487736),
            new ObjectLocation(50.397652, 30.509594),
            new ObjectLocation(50.393468, 30.489347),
            new ObjectLocation(50.382016, 30.477058),
            new ObjectLocation(50.376589, 30.468692),
            new ObjectLocation(50.366895, 30.453825),
            // red line
            new ObjectLocation(50.464379, 30.355252),
            new ObjectLocation(50.455885, 30.364319),
            new ObjectLocation(50.457818, 30.389476),
            new ObjectLocation(50.458490, 30.403157),
            new ObjectLocation(50.458714, 30.420383),
            new ObjectLocation(50.454914, 30.445283),
            new ObjectLocation(50.450711, 30.466285),
            new ObjectLocation(50.441693, 30.488128),
            new ObjectLocation(50.444380, 30.505942),
            new ObjectLocation(50.445225, 30.518040),
            new ObjectLocation(50.447316, 30.522752),
            new ObjectLocation(50.442946, 30.547617),
            new ObjectLocation(50.441117, 30.558650),
            new ObjectLocation(50.445721, 30.575935),
            new ObjectLocation(50.451639, 30.598251),
            new ObjectLocation(50.455807, 30.612921),
            new ObjectLocation(50.459850, 30.630305),
            new ObjectLocation(50.464864, 30.645560),
            // green line
            new ObjectLocation(50.476399, 30.430826),
            new ObjectLocation(50.476399, 30.430826),
            new ObjectLocation(50.462387, 30.481637),
            new ObjectLocation(50.448298, 30.513687),
            new ObjectLocation(50.439616, 30.519516),
            new ObjectLocation(50.436967, 30.531362),
            new ObjectLocation(50.427640, 30.538906),
            new ObjectLocation(50.418203, 30.544931),
            new ObjectLocation(50.401957, 30.560914),
            new ObjectLocation(50.394208, 30.604172),
            new ObjectLocation(50.395588, 30.616158),
            new ObjectLocation(50.398112, 30.633798),
            new ObjectLocation(50.401134, 30.652193),
            new ObjectLocation(50.403180, 30.665968),
            new ObjectLocation(50.403413, 30.683416),
            new ObjectLocation(50.409568, 30.696212)
    ));

    public static void main(String[] args) {
        System.out.println(FlatEarthDist.distance(50.450252d, 30.523875d,
                50.468288, 30.625937));

        System.out.println(FlatEarthDist.distance(50.450252d, 30.523875d,
                50.458241, 30.510369));
    }

    // returns true if location is in 1300m or less from one of the subway stations
    public static boolean nearSubway(double lat, double lng) {
        double minDistance = Double.MAX_VALUE;
        for (ObjectLocation subwayStation : subwayStations) {
            double distance = distance(subwayStation.getLatitude(), subwayStation.getLongitude(), lat, lng);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance <= 1300;
    }

    //returns distance in meters
    public static double distance(double lat1, double lng1,
                                  double lat2, double lng2){
        double a = (lat1-lat2)*FlatEarthDist.distPerLat(lat1);
        double b = (lng1-lng2)*FlatEarthDist.distPerLng(lat1);
        return Math.sqrt(a*a+b*b);
    }

    private static double distPerLng(double lat){
        return 0.0003121092*Math.pow(lat, 4)
                +0.0101182384*Math.pow(lat, 3)
                -17.2385140059*lat*lat
                +5.5485277537*lat+111301.967182595;
    }

    private static double distPerLat(double lat){
        return -0.000000487305676*Math.pow(lat, 4)
                -0.0033668574*Math.pow(lat, 3)
                +0.4601181791*lat*lat
                -1.4558127346*lat+110579.25662316;
    }
}