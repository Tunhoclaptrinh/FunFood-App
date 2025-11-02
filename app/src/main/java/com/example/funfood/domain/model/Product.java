package com.example.funfood.domain.model;

import java.util.Objects;

public class Product {
    private final String id;
    private final String name;
    private final String description;
    private final int price;
    private final String imageUrl;
    private final String restaurantId;
    private final String categoryId;
    private final int discount;
    private final boolean available;

    public Product(String id, String name, String description, int price, String imageUrl,
                   String restaurantId, String categoryId, int discount, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.restaurantId = restaurantId;
        this.categoryId = categoryId;
        this.discount = discount;
        this.available = available;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int getDiscount() {
        return discount;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getFinalPrice() {
        if (discount > 0) {
            return price - (price * discount / 100);
        }
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price &&
                discount == product.discount &&
                available == product.available &&
                Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(imageUrl, product.imageUrl) &&
                Objects.equals(restaurantId, product.restaurantId) &&
                Objects.equals(categoryId, product.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, imageUrl,
                restaurantId, categoryId, discount, available);
    }
}