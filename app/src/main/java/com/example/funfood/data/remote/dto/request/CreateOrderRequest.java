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

    public CreateOrderRequest(int restaurantId, List<OrderItem> items,
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
