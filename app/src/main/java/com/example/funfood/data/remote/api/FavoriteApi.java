package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.response.RestaurantResponse; // Hoặc FavoriteResponse
import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteApi {

    @GET("favorites")
    Call<ApiResponse<List<RestaurantResponse>>> getFavorites();

    @GET("favorites/restaurants")
    Call<ApiResponse<List<String>>> getFavoriteIds();

    @GET("favorites/check/{restaurantId}")
    Call<ApiResponse<Object>> checkFavorite(@Path("restaurantId") String restaurantId);

    @POST("favorites/{restaurantId}")
    Call<ApiResponse<Object>> addFavorite(@Path("restaurantId") String restaurantId);

    @POST("favorites/toggle/{restaurantId}")
    Call<ApiResponse<Object>> toggleFavorite(@Path("restaurantId") String restaurantId);

    @DELETE("favorites/{restaurantId}")
    Call<ApiResponse<Object>> removeFavorite(@Path("restaurantId") String restaurantId);
}