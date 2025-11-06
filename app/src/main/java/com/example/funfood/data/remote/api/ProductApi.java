package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    // Hàm này dùng để lấy sản phẩm nổi bật (tất cả nhà hàng)
    @GET("products")
    Call<ApiResponse<List<Product>>> getProducts(
            @Query("_page") int page,
            @Query("_limit") int limit
    );

    // Đổi tên hàm cũ hoặc thêm hàm mới để lấy sản phẩm theo nhà hàng
    @GET("products")
    Call<ApiResponse<List<Product>>> getProductsByRestaurant(
            @Query("restaurantId") int restaurantId,
            @Query("_page") int page,
            @Query("_limit") int limit
    );

    @GET("products/{id}")
    Call<ApiResponse<Product>> getProductById(
            @Path("id") int id,
            @Query("_expand") String expand // Thêm tham số _expand
    );

    @GET("restaurants/{id}/products")
    Call<ApiResponse<List<Product>>> getRestaurantProducts(
            @Path("id") int restaurantId,
            @Query("available") Boolean available
    );
}