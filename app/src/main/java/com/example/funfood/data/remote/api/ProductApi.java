package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.response.ProductResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {

    @GET("products")
    Call<ApiResponse<List<ProductResponse>>> getProducts(
            @Query("_page") int page,
            @Query("_limit") int limit,
            @Query("restaurantId") String restaurantId,
            @Query("categoryId") String categoryId,
            @Query("price_gte") Integer priceGte,
            @Query("price_lte") Integer priceLte,
            @Query("q") String searchTerm
    );

    @GET("products/search")
    Call<ApiResponse<List<ProductResponse>>> searchProducts(@Query("q") String query);

    @GET("products/{id}")
    Call<ApiResponse<ProductResponse>> getProductDetails(@Path("id") String productId);
}