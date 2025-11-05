package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Favorite;
import com.example.funfood.domain.model.Restaurant;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteApi {

    /**
     * Lấy danh sách nhà hàng yêu thích (có phân trang)
     */
    @GET("favorites")
    Call<ApiResponse<List<Favorite>>> getFavorites(
            @Query("_page") int page,
            @Query("_limit") int limit,
            @Query("_expand") String expand // ví dụ: "restaurant"
    );

    /**
     * Kiểm tra đã favorite chưa
     */
    @GET("favorites/check/{restaurantId}")
    Call<ApiResponse<Object>> checkFavorite(@Path("restaurantId") int restaurantId);

    /**
     * Thêm vào danh sách yêu thích
     */
    @POST("favorites/{restaurantId}")
    Call<ApiResponse<Object>> addFavorite(@Path("restaurantId") int restaurantId);

    /**
     * Xóa khỏi danh sách yêu thích
     */
    @DELETE("favorites/{restaurantId}")
    Call<ApiResponse<Object>> removeFavorite(@Path("restaurantId") int restaurantId);

    /**
     * Toggle (nếu đã yêu thích thì xóa, chưa thì thêm)
     */
    @POST("favorites/toggle/{restaurantId}")
    Call<ApiResponse<Object>> toggleFavorite(@Path("restaurantId") int restaurantId);
}
