package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.response.UserResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {

    @GET("users/{id}")
    Call<ApiResponse<UserResponse>> getUserById(@Path("id") int userId);

    @GET("users/{id}/activity")
    Call<ApiResponse<Object>> getUserActivity(@Path("id") int userId);

    // updateProfile và changePassword đã có trong AuthApi.java
}