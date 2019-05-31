package com.ml.util.data.pojo;

import java.util.Objects;

public class ApartmentData {
    private final String url;
    private String price;
    private String material;
    private String totalArea;
    private String roomsNumber;
    private String apartmentFloorNumber;
    private String maxFloorNumber;
    private String latitude;
    private String longitude;
    private String apartmentAge;
    private String distance;

    private ApartmentData(Builder builder) {
        this.url = builder.url;
        this.price = builder.price;
        this.material = builder.material;
        this.totalArea = builder.totalArea;;
        this.roomsNumber = builder.roomsNumber;
        this.apartmentFloorNumber = builder.apartmentFloorNumber;
        this.maxFloorNumber = builder.maxFloorNumber;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.apartmentAge = builder.apartmentAge;
        this.distance = String.valueOf(FlatEarthDist.distance(50.450252d, 30.523875d,
                Double.valueOf(latitude), Double.valueOf(longitude)));
    }

    public static class Builder {
        private final String url;
        private String price;
        private String material;
        private String totalArea;
        private String roomsNumber;
        private String apartmentFloorNumber;
        private String maxFloorNumber;
        private String latitude;
        private String longitude;
        private String apartmentAge;

        public Builder(String url) {
            this.url = url;
        }

        public Builder setPrice(String price) {
            this.price = price;
            return this;
        }

        public Builder setMaterial(String material) {
            this.material = material;
            return this;
        }

        public Builder setTotalArea(String totalArea) {
            this.totalArea = totalArea;
            return this;
        }

        public Builder setRoomsNumber(String roomsNumber) {
            this.roomsNumber = roomsNumber;
            return this;
        }

        public Builder setApartmentFloorNumber(String apartmentFloorNumber) {
            this.apartmentFloorNumber = apartmentFloorNumber;
            return this;
        }

        public Builder setMaxFloorNumber(String maxFloorNumber) {
            this.maxFloorNumber = maxFloorNumber;
            return this;
        }

        public Builder setLatitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setApartmentAge(String apartmentAge) {
            this.apartmentAge = apartmentAge;
            return this;
        }

        public ApartmentData build() {
            return new ApartmentData(this);
        }
    }

    @Override
    public String toString() {
        return url + "," +
                material + "," +
                totalArea + "," +
                roomsNumber + "," +
                Double.valueOf(apartmentFloorNumber) / Double.valueOf(maxFloorNumber) + "," +
                distance + "," +
                apartmentAge + "," +
                price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApartmentData that = (ApartmentData) o;
        return Objects.equals(price, that.price) &&
                Objects.equals(totalArea, that.totalArea) &&
                Objects.equals(roomsNumber, that.roomsNumber) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, totalArea, roomsNumber, latitude, longitude);
    }

    public boolean isCorrect() {
        return !(material == null || material.equals("null")) &&
                !(totalArea == null || totalArea.equals("null")) &&
                !(roomsNumber == null || roomsNumber.equals("null")) &&
                !(apartmentFloorNumber == null || apartmentFloorNumber.equals("null")
                    || apartmentFloorNumber.equals("цоколь") || apartmentFloorNumber.equals("бельэтаж")
                    || apartmentFloorNumber.equals("полуподвал")) &&
                !(maxFloorNumber == null || maxFloorNumber.equals("null")) &&
                !(apartmentAge == null || apartmentAge.equals("null"));
    }
}
