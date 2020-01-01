package com.ml.util.data.pojo;

public class ObjectLocation {
    private double latitude;
    private double longitude;

    public ObjectLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
