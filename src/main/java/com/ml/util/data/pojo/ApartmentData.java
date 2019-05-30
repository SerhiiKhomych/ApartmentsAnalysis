package com.ml.util.data.pojo;

public class ApartmentData {
    private String price;
    private String material;
    private String totalArea;
    private String roomsNumber;
    private String apartmentFloorNumber;
    private String maxFloorNumber;
    private String latitude;
    private String longitude;
    private String apartmentAge;

    private ApartmentData(String price, String material, String totalArea, String roomsNumber, String apartmentFloorNumber, String maxFloorNumber, String latitude, String longitude, String apartmentAge) {
        this.price = price;
        this.material = material;
        this.totalArea = totalArea;
        this.roomsNumber = roomsNumber;
        this.apartmentFloorNumber = apartmentFloorNumber;
        this.maxFloorNumber = maxFloorNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.apartmentAge = apartmentAge;
    }

    private ApartmentData(Builder builder) {
        this.price = builder.price;
        this.material = builder.material;
        this.totalArea = builder.totalArea;;
        this.roomsNumber = builder.roomsNumber;
        this.apartmentFloorNumber = builder.apartmentFloorNumber;
        this.maxFloorNumber = builder.maxFloorNumber;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.apartmentAge = builder.apartmentAge;
    }

    public static class Builder {
        private String price;
        private String material;
        private String totalArea;
        private String roomsNumber;
        private String apartmentFloorNumber;
        private String maxFloorNumber;
        private String latitude;
        private String longitude;
        private String apartmentAge;

        public Builder() {
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

    public String getPrice() {
        return price;
    }

    public String getMaterial() {
        return material;
    }

    public String getTotalArea() {
        return totalArea;
    }

    public String getRoomsNumber() {
        return roomsNumber;
    }

    public String getApartmentFloorNumber() {
        return apartmentFloorNumber;
    }

    public String getMaxFloorNumber() {
        return maxFloorNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getApartmentAge() {
        return apartmentAge;
    }

    @Override
    public String toString() {
        return "ApartmentData{" +
                "price='" + price + '\'' +
                ", material='" + material + '\'' +
                ", totalArea='" + totalArea + '\'' +
                ", roomsNumber='" + roomsNumber + '\'' +
                ", apartmentFloorNumber='" + apartmentFloorNumber + '\'' +
                ", maxFloorNumber='" + maxFloorNumber + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", apartmentAge='" + apartmentAge + '\'' +
                '}';
    }
}
