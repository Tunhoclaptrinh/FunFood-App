package com.example.funfood.domain.model;
import com.google.gson.annotations.SerializedName;
public class OrderItem {
    @SerializedName("productId")
    private int productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price")
    private double price;

    @SerializedName("discount")
    private double discount;

    public OrderItem() {
    }

    public OrderItem(int productId, String productName, int quantity, int price, int discount) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }


    // Getters and setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public double getDiscount() { return discount; }
    public void setDiscount(int discount) { this.discount = discount; }
}
