package com.example.funfood.domain.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class Cart {
    @SerializedName("items")
    private List<CartItem> items;

    @SerializedName("groupedByRestaurant")
    private Map<String, CartGroup> groupedByRestaurant;

    @SerializedName("summary")
    private CartSummary summary;

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public Map<String, CartGroup> getGroupedByRestaurant() { return groupedByRestaurant; }
    public void setGroupedByRestaurant(Map<String, CartGroup> groupedByRestaurant) {
        this.groupedByRestaurant = groupedByRestaurant;
    }

    public CartSummary getSummary() { return summary; }
    public void setSummary(CartSummary summary) { this.summary = summary; }

    public static class CartGroup {
        @SerializedName("restaurant")
        private Restaurant restaurant;

        @SerializedName("items")
        private List<CartItem> items;

        @SerializedName("subtotal")
        private int subtotal;

        public Restaurant getRestaurant() { return restaurant; }
        public List<CartItem> getItems() { return items; }
        public int getSubtotal() { return subtotal; }
    }

    public static class CartSummary {
        @SerializedName("totalItems")
        private int totalItems;

        @SerializedName("subtotal")
        private int subtotal;

        @SerializedName("deliveryFee")
        private int deliveryFee;

        @SerializedName("total")
        private int total;

        public int getTotalItems() { return totalItems; }
        public int getSubtotal() { return subtotal; }
        public int getDeliveryFee() { return deliveryFee; }
        public int getTotal() { return total; }
    }
}
