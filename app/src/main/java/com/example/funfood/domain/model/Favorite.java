package com.example.funfood.domain.model;

import com.google.gson.annotations.SerializedName;

public class Favorite {

    @SerializedName("id")
    private int id;

    @SerializedName("restaurantId")
    private int restaurantId;

    // Khi dùng ?_expand=restaurant thì API trả thêm object restaurant
    @SerializedName("restaurant")
    private Restaurant restaurant;

    @SerializedName("createdAt")
    private String createdAt;

    public Favorite() {}

    public Favorite(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return restaurant != null ? restaurant.getName() : "Restaurant ID: " + restaurantId;
    }
}
