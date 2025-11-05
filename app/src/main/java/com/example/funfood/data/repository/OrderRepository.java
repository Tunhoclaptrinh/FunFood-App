package com.example.funfood.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.OrderApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Order;
import com.example.funfood.util.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {

    private final OrderApi orderApi;

    public OrderRepository(Context context) {
        this.orderApi = RetrofitClient.getInstance(context).createService(OrderApi.class);
    }

    /**
     * Lấy danh sách đơn hàng của user
     */
    public LiveData<Resource<List<Order>>> getOrders(int page, int limit) {
        MutableLiveData<Resource<List<Order>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        orderApi.getOrders(page, limit).enqueue(new Callback<ApiResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Order>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể tải đơn hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Lấy chi tiết đơn hàng
     */
    public LiveData<Resource<Order>> getOrderById(int orderId) {
        MutableLiveData<Resource<Order>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        orderApi.getOrderById(orderId).enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Order> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể tải chi tiết đơn hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Tạo đơn hàng mới
     */
    public LiveData<Resource<Order>> createOrder(CreateOrderRequest request) {
        MutableLiveData<Resource<Order>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        orderApi.createOrder(request).enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Order> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể tạo đơn hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Hủy đơn hàng
     */
    public LiveData<Resource<Void>> cancelOrder(int orderId) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        orderApi.cancelOrder(orderId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(null));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể hủy đơn hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Request DTO cho tạo đơn hàng
     */
    public static class CreateOrderRequest {
        private int restaurantId;
        private java.util.List<OrderItem> items;
        private String deliveryAddress;
        private double deliveryLatitude;
        private double deliveryLongitude;
        private String paymentMethod;
        private String note;
        private String promotionCode;

        public CreateOrderRequest(int restaurantId, java.util.List<OrderItem> items,
                                  String deliveryAddress, double latitude, double longitude,
                                  String paymentMethod, String note, String promotionCode) {
            this.restaurantId = restaurantId;
            this.items = items;
            this.deliveryAddress = deliveryAddress;
            this.deliveryLatitude = latitude;
            this.deliveryLongitude = longitude;
            this.paymentMethod = paymentMethod;
            this.note = note;
            this.promotionCode = promotionCode;
        }

        // Getters
        public int getRestaurantId() { return restaurantId; }
        public java.util.List<OrderItem> getItems() { return items; }
        public String getDeliveryAddress() { return deliveryAddress; }
        public double getDeliveryLatitude() { return deliveryLatitude; }
        public double getDeliveryLongitude() { return deliveryLongitude; }
        public String getPaymentMethod() { return paymentMethod; }
        public String getNote() { return note; }
        public String getPromotionCode() { return promotionCode; }
    }

    /**
     * Order Item DTO
     */
    public static class OrderItem {
        private int productId;
        private int quantity;

        public OrderItem(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public int getProductId() { return productId; }
        public int getQuantity() { return quantity; }
    }
}