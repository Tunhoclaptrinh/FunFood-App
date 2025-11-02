package com.example.funfood.domain.model;

import java.util.Objects;

public class Promotion {
    private final String id;
    private final String imageUrl;
    private final String description;

    public Promotion(String id, String imageUrl, String description) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promotion promotion = (Promotion) o;
        return Objects.equals(id, promotion.id) &&
                Objects.equals(imageUrl, promotion.imageUrl) &&
                Objects.equals(description, promotion.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageUrl, description);
    }
}