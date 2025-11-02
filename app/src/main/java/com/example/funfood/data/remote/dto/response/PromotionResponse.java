package com.example.funfood.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class PromotionResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("code")
    private String code;

    @SerializedName("description")
    private String description;

    @SerializedName("image") // Giả sử API trả về trường 'image' cho URL ảnh
    private String imageUrl;

    @SerializedName("discountType")
    private String discountType;

    @SerializedName("discountValue")
    private double discountValue;

    @SerializedName("minOrderValue")
    private double minOrderValue;

    @SerializedName("validTo")
    private String validTo;

    // Getters
    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDiscountType() {
        return discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public String getValidTo() {
        return validTo;
    }
}