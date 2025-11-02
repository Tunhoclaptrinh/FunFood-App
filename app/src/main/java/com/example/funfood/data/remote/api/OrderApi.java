package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.request.CreateOrderRequest;
import com.example.funfood.data.remote.dto.response.OrderResponse;
import com.google.gson.JsonObject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderApi {

    @GET("orders")
    Call<ApiResponse<List<OrderResponse>>> getMyOrders(
            @Query("_page") int page,
            @Query("_limit") int limit,
            @Query("status") String status
    );

    @GET("orders/all")
    Call<ApiResponse<List<OrderResponse>>> getAllOrders(@Query("_page") int page, @Query("_limit") int limit);

    @GET("orders/{id}")
    Call<ApiResponse<OrderResponse>> getOrderDetails(@Path("id") String orderId);

    @POST("orders")
    Call<ApiResponse<OrderResponse>> createOrder(@Body CreateOrderRequest request);

    // Giả sử StatusRequest có dạng: { status: "cancelled" }
    @PATCH("orders/{id}/status")
    Call<ApiResponse<OrderResponse>> updateOrderStatus(@Path("id") String orderId, @Body JsonObject statusRequest);

    @DELETE("orders/{id}")
    Call<ApiResponse<Object>> cancelOrder(@Path("id") String orderId);
}