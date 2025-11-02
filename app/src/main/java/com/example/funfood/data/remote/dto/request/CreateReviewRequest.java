package com.example.funfood.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class CreateReviewRequest {

    @SerializedName("restaurantId")
    private int restaurantId;

    @SerializedName("orderId")
    private int orderId;

    @SerializedName("rating")
    private int rating;

    @SerializedName("comment")
    private String comment;

    public CreateReviewRequest(int restaurantId, int orderId, int rating, String comment) {
        this.restaurantId = restaurantId;
        this.orderId = orderId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}