package com.example.funfood.domain.model;
import java.util.List;
import com.google.gson.annotations.SerializedName;
public class Order {
    private int id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("restaurantId")
    private int restaurantId;

    @SerializedName("items")
    private List<OrderItem> items;

    @SerializedName("subtotal")
    private double subtotal;

    @SerializedName("deliveryFee")
    private double deliveryFee;

    @SerializedName("discount")
    private double discount;

    @SerializedName("total")
    private double total;

    @SerializedName("status")
    private String status;

    @SerializedName("deliveryAddress")
    private String deliveryAddress;

    @SerializedName("deliveryLatitude")
    private double deliveryLatitude;

    @SerializedName("deliveryLongitude")
    private double deliveryLongitude;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    @SerializedName("note")
    private String note;

    @SerializedName("promotionCode")
    private String promotionCode;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    public Order() {
    }
    public Order(int id, int userId, int restaurantId, List<OrderItem> items, double subtotal, double deliveryFee, double discount, double total, String status, String deliveryAddress, double deliveryLatitude, double deliveryLongitude, String paymentMethod, String note , String promotionCode) {
    this.id = id;
    this.userId = userId;
    this.restaurantId = restaurantId;
    this.items = items;
    this.subtotal = subtotal;
    this.deliveryFee = deliveryFee;
    this.discount = discount;
    this.total = total;
    this.status = status;
    this.deliveryAddress = deliveryAddress;
    this.deliveryLatitude = deliveryLatitude;
    this.deliveryLongitude = deliveryLongitude;
    this.paymentMethod = paymentMethod;
    this.note = note;
    this.promotionCode = promotionCode;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(int subtotal) { this.subtotal = subtotal; }

    public double getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(int deliveryFee) { this.deliveryFee = deliveryFee; }

    public double getDiscount() { return discount; }
    public void setDiscount(int discount) { this.discount = discount; }

    public double getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public double getDeliveryLatitude() { return deliveryLatitude; }
    public void setDeliveryLatitude(double deliveryLatitude) { this.deliveryLatitude = deliveryLatitude; }

    public double getDeliveryLongitude() { return deliveryLongitude; }
    public void setDeliveryLongitude(double deliveryLongitude) { this.deliveryLongitude = deliveryLongitude; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getPromotionCode() { return promotionCode; }
    public void setPromotionCode(String promotionCode) { this.promotionCode = promotionCode; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
