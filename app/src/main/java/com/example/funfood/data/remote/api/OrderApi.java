package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.repository.OrderRepository;
import com.example.funfood.domain.model.Order;

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
    Call<ApiResponse<List<Order>>> getOrders(
            @Query("_page") int page,
            @Query("_limit") int limit
    );

    @GET("orders/{id}")
    Call<ApiResponse<Order>> getOrderById(@Path("id") int orderId);

    @POST("orders")
    Call<ApiResponse<Order>> createOrder(@Body OrderRepository.CreateOrderRequest request);

    @PATCH("orders/{id}/status")
    Call<ApiResponse<Void>> updateOrderStatus(
            @Path("id") int orderId,
            @Body UpdateOrderStatusRequest request
    );

    @DELETE("orders/{id}")
    Call<ApiResponse<Void>> cancelOrder(@Path("id") int orderId);

    // Request DTO
    class UpdateOrderStatusRequest {
        private String status;

        public UpdateOrderStatusRequest(String status) {
            this.status = status;
        }

        public String getStatus() { return status; }
    }
}
