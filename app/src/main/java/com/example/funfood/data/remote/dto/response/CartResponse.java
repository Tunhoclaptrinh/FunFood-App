package com.example.funfood.data.remote.dto.response;

import com.example.funfood.domain.model.CartItem;
import com.example.funfood.domain.model.Restaurant;
import com.google.gson.annotations.SerializedName;
import java.util.List;

// Lớp này đại diện cho đối tượng "data" trong JSON
public class CartResponse {

    @SerializedName("items")
    private List<CartItem> items;

    @SerializedName("groupedByRestaurant")
    private List<GroupedRestaurant> groupedByRestaurant;

    @SerializedName("summary")
    private CartSummary summary;

    // Getters
    public List<CartItem> getItems() {
        return items;
    }

    public List<GroupedRestaurant> getGroupedByRestaurant() {
        return groupedByRestaurant;
    }

    public CartSummary getSummary() {
        return summary;
    }

    // Lớp con cho "groupedByRestaurant"
    public static class GroupedRestaurant {

        @SerializedName("restaurant")
        private Restaurant restaurant;

        @SerializedName("items")
        private List<CartItem> items;

        @SerializedName("subtotal")
        private double subtotal;

        // Getters
        public Restaurant getRestaurant() {
            return restaurant;
        }

        public List<CartItem> getItems() {
            return items;
        }

        public double getSubtotal() {
            return subtotal;
        }
    }

    // Lớp con cho "summary"
    public static class CartSummary {

        @SerializedName("totalItems")
        private int totalItems;

        @SerializedName("subtotal")
        private double subtotal;

        @SerializedName("deliveryFee")
        private double deliveryFee;

        @SerializedName("total")
        private double total;

        // Getters
        public int getTotalItems() {
            return totalItems;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public double getDeliveryFee() {
            return deliveryFee;
        }

        public double getTotal() {
            return total;
        }
    }
}