package com.example.funfood.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class ReviewResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("restaurantId")
    private int restaurantId;

    @SerializedName("rating")
    private int rating;

    @SerializedName("comment")
    private String comment;

    @SerializedName("createdAt")
    private String createdAt;

    // Dựa theo API_ENDPOINTS.md,
    // response có một object user lồng bên trong.
    // Chúng ta có thể dùng lại UserResponse có sẵn.
    @SerializedName("user")
    private UserResponse user;

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserResponse getUser() {
        return user;
    }
}