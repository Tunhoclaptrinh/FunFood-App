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

import retrofit2.Call;

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
    //Load danh sách đơn hàng theo userId và bộ lọc trạng thái (status)
    /**
     * 🟢 Lấy danh sách đơn hàng của user (có thể lọc theo status)
     */
    public void loadOrders(int userId, String status, int page) {
        loadingLiveData.setValue(true);

        orderRepository.getMyOrders(userId, status, page).observeForever(response -> {
            loadingLiveData.setValue(false);
            ordersLiveData.setValue(response);
        });
    }

    public void loadFilterOrders(){};

    public void filterOrders(String status) {
        currentFilter = status;
        applyFilter(status);
    }

    private void applyFilter(String status){}
}
