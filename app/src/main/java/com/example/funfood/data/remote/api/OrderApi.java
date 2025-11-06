package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.request.CreateOrderRequest;
import com.example.funfood.data.remote.dto.request.UpdateOrderStatusRequest;
import com.example.funfood.domain.model.Order;
import com.google.android.gms.common.api.Api;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface OrderApi {
    //   Lấy danh sách tất cả đơn hàng
    @GET("orders")
    Call<ApiResponse<List<Order>>> getMyOrders(
//            @QueryMap Map<String, String> filters
            @Query("_page") int page,
            @Query("_limit") int limit
    );

    // 🟩 2. Lấy tất cả đơn hàng (Admin)
    // /api/orders/all?userId=2&status=pending
    @GET("orders/all")
    Call<ApiResponse<List<Order>>> getAllOrders(
            @QueryMap Map<String, String> filters
    );


    //  Lấy danh sách đơn hàng của một user cụ thể
    @GET("orders")
    Call<ApiResponse<List<Order>>> getOrdersByUser(@Query("userId") int userId);

    //  Lấy chi tiết một đơn hàng theo ID
    @GET("orders/{id}")
    Call<ApiResponse<Order>> getOrderById(@Path("id") int id);

    //  Tạo đơn hàng mới
    @POST("orders")
    Call<ApiResponse<Order>> createOrder(@Body CreateOrderRequest order);

    //  Cập nhật đơn hàng (VD: đổi trạng thái, chỉnh ghi chú, v.v.)
    @PUT("orders/{id}")
    Call<ApiResponse<Order>> updateOrder(@Path("id") int id, @Body Order order);

    // . Cập nhật trạng thái đơn hàng (PATCH /api/orders/:id/status)
    // Access: Owner (cancel) hoặc Admin
    @PATCH("orders/{id}/status")
    Call<ApiResponse<Order>> updateOrderStatus(
            @Path("id") int id,
            @Body UpdateOrderStatusRequest body
    );

    //  Xoá đơn hàng
    @DELETE("orders/{id}")
    Call<ApiResponse<Void>> deleteOrder(@Path("id") int id);
}
