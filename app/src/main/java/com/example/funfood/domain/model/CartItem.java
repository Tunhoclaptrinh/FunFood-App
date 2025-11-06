package com.example.funfood.domain.model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("id")
    private int id;

    @SerializedName("productId")
    private int productId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("product")
    private Product product;

    @SerializedName("restaurant")
    private Restaurant restaurant;

    @SerializedName("itemTotal")
    private int itemTotal;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public int getItemTotal() { return itemTotal; }
    public void setItemTotal(int itemTotal) { this.itemTotal = itemTotal; }
}