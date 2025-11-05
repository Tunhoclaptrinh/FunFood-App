package com.example.funfood.domain.model;

import java.util.List;

public class Order {
    private int id;
    private int restaurantId;
    private List<OrderItem> items; // Bạn cũng cần đảm bảo model OrderItem tồn tại
    private double subtotal;
    private double deliveryFee;
    private double discount;
    private double total;
    private String status;
    private String deliveryAddress;
    private double deliveryLatitude;
    private double deliveryLongitude;
    private String createdAt;
    private String note;
    private String paymentMethod;
    private User user; // Có thể cần nếu API trả về
    private Restaurant restaurant; // Có thể cần nếu API trả về

    // Constructor (Tùy chọn)
    public Order() {
    }

    // --- GETTERS ---
    // Đây là phương thức quan trọng nhất để sửa lỗi của bạn
    public int getId() {
        return id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public double getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public double getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getNote() {
        return note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}