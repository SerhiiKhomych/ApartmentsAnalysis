package com.ml.util.data.pojo;

import com.github.davidmoten.geo.GeoHash;

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
    private String geoHash;

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
        this.geoHash = GeoHash.encodeHash(Double.valueOf(latitude), Double.valueOf(longitude));
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
                price + "," +
                material + "," +
                totalArea + "," +
                roomsNumber + "," +
                Double.valueOf(apartmentFloorNumber) / Double.valueOf(maxFloorNumber) + "," +
                geoHash + "," +
                apartmentAge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApartmentData that = (ApartmentData) o;
        return Objects.equals(price, that.price) &&
                Objects.equals(totalArea, that.totalArea) &&
                Objects.equals(roomsNumber, that.roomsNumber) &&
                Objects.equals(geoHash, that.geoHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, totalArea, roomsNumber, geoHash);
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
