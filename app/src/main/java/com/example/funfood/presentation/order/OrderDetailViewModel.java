package com.example.funfood.presentation.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.repository.OrderRepository;
import com.example.funfood.domain.model.Order;
import com.example.funfood.util.Resource;

public class OrderDetailViewModel extends AndroidViewModel { // FIX: Phải extends AndroidViewModel

    private final OrderRepository orderRepository;
    private final MutableLiveData<Resource<Order>> orderLiveData = new MutableLiveData<>();

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        this.orderRepository = new OrderRepository(application);
    }

    public LiveData<Resource<Order>> getOrderLiveData() {
        return orderLiveData;
    }

    /**
     * Tải chi tiết đơn hàng
     */
    public void loadOrder(int orderId) {
        orderLiveData.setValue(Resource.loading(null));
        orderRepository.getOrderById(orderId).observeForever(resource -> {
            orderLiveData.setValue(resource);
        });
    }
}