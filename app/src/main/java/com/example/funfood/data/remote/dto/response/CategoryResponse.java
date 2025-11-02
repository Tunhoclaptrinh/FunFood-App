package com.example.funfood.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class CategoryResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getImage() { return image; }
}