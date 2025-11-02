package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("products")
    Call<ApiResponse<List<Product>>> getProducts(
            @Query("restaurantId") int restaurantId,
            @Query("_page") int page,
            @Query("_limit") int limit
    );

    @GET("products/{id}")
    Call<ApiResponse<Product>> getProductById(@Path("id") int id);

    @GET("restaurants/{id}/products")
    Call<ApiResponse<List<Product>>> getRestaurantProducts(
            @Path("id") int restaurantId,
            @Query("available") Boolean available
    );
}