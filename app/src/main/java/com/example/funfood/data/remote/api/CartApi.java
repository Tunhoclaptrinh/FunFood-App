package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.request.AddToCartRequest;
import com.example.funfood.data.remote.dto.response.CartResponse;
import com.google.gson.JsonObject; // Sử dụng JsonObject cho request body đơn giản

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartApi {

    @GET("cart")
    Call<ApiResponse<CartResponse>> getCart();

    @POST("cart")
    Call<ApiResponse<CartResponse>> addToCart(@Body AddToCartRequest request);

    // Giả sử SyncCartRequest có dạng: { items: List<CartItemDto> }
    @POST("cart/sync")
    Call<ApiResponse<CartResponse>> syncCart(@Body JsonObject syncRequest);

    // Giả sử UpdateQtyRequest có dạng: { quantity: int }
    @PUT("cart/{id}")
    Call<ApiResponse<CartResponse>> updateCartItem(@Path("id") int cartItemId, @Body JsonObject updateQtyRequest);

    @DELETE("cart/{id}")
    Call<ApiResponse<CartResponse>> deleteCartItem(@Path("id") int cartItemId);

    @DELETE("cart/restaurant/{restaurantId}")
    Call<ApiResponse<CartResponse>> clearCartByRestaurant(@Path("restaurantId") String restaurantId);

    @DELETE("cart")
    Call<ApiResponse<Object>> clearCart();
}