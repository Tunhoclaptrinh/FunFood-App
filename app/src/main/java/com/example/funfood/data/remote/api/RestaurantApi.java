package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Restaurant;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantApi {
    @GET("restaurants")
    Call<ApiResponse<List<Restaurant>>> getRestaurants(
            @Query("_page") int page,
            @Query("_limit") int limit
    );

    @GET("restaurants/{id}")
    Call<ApiResponse<Restaurant>> getRestaurantById(@Path("id") int id);

    @GET("restaurants/search")
    Call<ApiResponse<List<Restaurant>>> searchRestaurants(@Query("q") String query);

    @GET("restaurants/nearby")
    Call<ApiResponse<List<Restaurant>>> getNearbyRestaurants(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("radius") double radius
    );
}