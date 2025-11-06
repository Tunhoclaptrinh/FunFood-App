package com.example.funfood.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.funfood.data.preferences.UserPreferences;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.OrderApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.request.CreateOrderRequest;
import com.example.funfood.data.remote.dto.request.UpdateOrderStatusRequest;
import com.example.funfood.domain.model.Category;
import com.example.funfood.domain.model.Order;
import com.example.funfood.util.Resource;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository quản lý toàn bộ việc gọi API liên quan đến Order.
 * Cung cấp LiveData cho ViewModel để dễ dàng quan sát (observe) trong UI.
 */
public class OrderRepository {

    private final OrderApi orderApi;
//    private final UserPreferences userPreferences;

    public OrderRepository(Context context) {
        orderApi = RetrofitClient.getInstance(context).createService(OrderApi.class);
    }

    /**
     * 🟩 Lấy danh sách đơn hàng của người dùng (có thể filter).
     */

//
    // 🟢 1. Lấy danh sách đơn hàng của user hiện tại
    public LiveData<ApiResponse<List<Order>>> getMyOrders(int userId, String status, int page) {
        MutableLiveData<ApiResponse<List<Order>>> data = new MutableLiveData<>();
        int limit = 20;
        Call<ApiResponse<List<Order>>> call = orderApi.getMyOrders(page, limit);
        call.enqueue(new Callback<ApiResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Order> allOrders = response.body().getData();
                    List<Order> filteredOrders = new ArrayList<>();

                    if (status.equalsIgnoreCase("all")) {
                        filteredOrders = allOrders;
                    } else {
                        for (Order order : allOrders) {
                            if (order.getStatus() != null && order.getStatus().equalsIgnoreCase(status)) {
                                filteredOrders.add(order);
                            }
                        }
                    }

                    data.setValue(new ApiResponse<>(true, "Lấy danh sách đơn hàng thành công", filteredOrders));
                } else {
                    data.setValue(new ApiResponse<>(false, "Không thể tải danh sách đơn hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                data.setValue(new ApiResponse<>(false, t.getMessage(), null));
            }
        });
        return data;
    }

    // 🟢 2. Lấy tất cả đơn hàng (Admin)
    public LiveData<ApiResponse<List<Order>>> getAllOrders(Map<String, String> filters) {
        MutableLiveData<ApiResponse<List<Order>>> data = new MutableLiveData<>();
        orderApi.getAllOrders(filters).enqueue(new Callback<ApiResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ApiResponse<>(false, "Failed to load all orders", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                data.setValue(new ApiResponse<>(false, t.getMessage(), null));
            }
        });
        return data;
    }

    // 🟢 3. Lấy chi tiết một đơn hàng
    public LiveData<ApiResponse<Order>> getOrderById(int id) {
        MutableLiveData<ApiResponse<Order>> data = new MutableLiveData<>();
        orderApi.getOrderById(id).enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ApiResponse<>(false, "Failed to load order detail", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                data.setValue(new ApiResponse<>(false, t.getMessage(), null));
            }
        });
        return data;
    }

    // 🟢 4. Tạo đơn hàng mới
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

    // 🟢 5. Cập nhật trạng thái đơn hàng
    public LiveData<ApiResponse<Order>> updateOrderStatus(int id, UpdateOrderStatusRequest statusBody) {
        MutableLiveData<ApiResponse<Order>> data = new MutableLiveData<>();
        orderApi.updateOrderStatus(id, statusBody).enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ApiResponse<>(false, "Failed to update order status", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                data.setValue(new ApiResponse<>(false, t.getMessage(), null));
            }
        });
        return data;
    }

    // 🟢 6. Hủy đơn hàng
    public LiveData<ApiResponse<Void>> deleteOrder(int id) {
        MutableLiveData<ApiResponse<Void>> data = new MutableLiveData<>();
        orderApi.deleteOrder(id).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(new ApiResponse<>(false, "Failed to cancel order", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                data.setValue(new ApiResponse<>(false, t.getMessage(), null));
            }
        });
        return data;


    }
}

