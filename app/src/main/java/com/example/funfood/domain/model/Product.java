package com.example.funfood.domain.model;

public class Product {
    private int id;
    private String name;
    private String description;
    private int price;
    private String image;
    private int restaurantId;
    private int categoryId;
    private boolean available;
    private int discount; // Percentage discount

    public Product() {}

    private Restaurant restaurant;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public int getDiscount() { return discount; }
    public void setDiscount(int discount) { this.discount = discount; }

    // Helper methods
    public int getDiscountedPrice() {
        if (discount > 0) {
            return price - (price * discount / 100);
        }
        return price;
    }

    public boolean hasDiscount() {
        return discount > 0;
    }
}