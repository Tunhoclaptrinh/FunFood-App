package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.response.ProductResponse;
import com.example.funfood.data.remote.dto.response.RestaurantResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantApi {

    @GET("restaurants")
    Call<ApiResponse<List<RestaurantResponse>>> getRestaurants(
            @Query("_page") int page,
            @Query("_limit") int limit,
            @Query("categoryId") String categoryId,
            @Query("isOpen") Boolean isOpen,
            @Query("rating_gte") Double ratingGte,
            @Query("q") String searchTerm
    );

    @GET("restaurants/nearby")
    Call<ApiResponse<List<RestaurantResponse>>> getNearbyRestaurants(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("radius") double radius,
            @Query("_page") int page,
            @Query("_limit") int limit
    );

    @GET("restaurants/{id}")
    Call<ApiResponse<RestaurantResponse>> getRestaurantDetails(
            @Path("id") String restaurantId,
            @Query("_embed") List<String> embed // "products", "reviews"
    );

    @GET("restaurants/{id}/products")
    Call<ApiResponse<List<ProductResponse>>> getRestaurantProducts(
            @Path("id") String restaurantId
    );
}