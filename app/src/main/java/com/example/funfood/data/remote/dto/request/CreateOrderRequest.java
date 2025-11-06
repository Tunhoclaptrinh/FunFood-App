package com.example.funfood.data.remote.dto.request;

import com.example.funfood.domain.model.OrderItem;

import java.util.List;

public class CreateOrderRequest {
    int restaurantId;
    List<OrderItem> items;
    String deliveryAddress;
    double deliveryLatitude;
    double deliveryLongitude;
    String paymentMethod;
    String note;
    String promotionCode;
}
