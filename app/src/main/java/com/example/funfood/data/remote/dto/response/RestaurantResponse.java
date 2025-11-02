package com.example.funfood.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class RestaurantResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("rating")
    private double rating;

    @SerializedName("image") // Giả sử API trả về trường 'image'
    private String imageUrl;

    @SerializedName("distance") // Giả sử API trả về 'distance' (nếu có)
    private double distance;

    @SerializedName("deliveryFee")
    private int deliveryFee;

    @SerializedName("deliveryTime")
    private String deliveryTime;

    @SerializedName("isOpen")
    private boolean isOpen;

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getDistance() {
        return distance;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public boolean isOpen() {
        return isOpen;
    }
}