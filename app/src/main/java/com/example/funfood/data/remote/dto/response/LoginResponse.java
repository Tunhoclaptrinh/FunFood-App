package com.example.funfood.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("user")
    private UserResponse user;

    @SerializedName("token")
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(UserResponse user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}