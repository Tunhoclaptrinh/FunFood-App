package com.example.funfood.presentation.main.orders;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.OrderApi;
import com.example.funfood.presentation.base.BaseViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.repository.OrderRepository;
import com.example.funfood.domain.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersViewModel extends BaseViewModel {
    private final OrderRepository orderRepository;
    private final MutableLiveData<ApiResponse<List<Order>>> ordersLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Order>> filteredOrdersLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> cancelSuccessLiveData = new MutableLiveData<>();

    private List<Order> allOrders = new ArrayList<>();
    private String currentFilter = "all";

    public OrdersViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application.getApplicationContext());
        loadFilterOrders();
    }

    public LiveData<ApiResponse<List<Order>>> getOrdersLiveData() {
        return ordersLiveData;
    }

    public LiveData<List<Order>> getOrders() {
        return filteredOrdersLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getCancelSuccess() {
        return cancelSuccessLiveData;
    }

    /**
     * 🟢 Lấy danh sách đơn hàng của user (có thể lọc theo status)
     */
    public void loadOrders(int userId, String status, int page) {
        loadingLiveData.setValue(true);

        Map<String, String> filters = new HashMap<>();
        filters.put("userId", String.valueOf(userId));
        filters.put("_page", String.valueOf(page));
        filters.put("_limit", "10");
        filters.put("_sort", "createdAt");
        filters.put("_order", "desc");

        if (status != null && !status.equalsIgnoreCase("all")) {
            filters.put("status", status.toLowerCase());
        }

        // Gọi chung một hàm getMyOrders với filters
        orderRepository.getMyOrders(filters).observeForever(response -> {
            loadingLiveData.setValue(false);
            if (response.isSuccess() && response.getData() != null) {
                allOrders = response.getData();
                ordersLiveData.setValue(response);
                applyFilter(currentFilter);
            } else {
                ordersLiveData.setValue(response);
                setError(response.getMessage());
                errorLiveData.setValue("Không thể tải danh sách đơn hàng");
            }
        });
    }

    public void loadFilterOrders(){};

    public void filterOrders(String status) {
        currentFilter = status;
        applyFilter(status);
    }

    private void applyFilter(String status){}
}
