package com.example.funfood.domain.model;

import java.util.Objects;

public class Review {
    private final int id;
    private final int rating;
    private final String comment;
    private final String createdAt;
    private final String userName;
    private final String userAvatar;

    public Review(int id, int rating, String comment, String createdAt, String userName, String userAvatar) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userName = userName;
        this.userAvatar = userAvatar;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id &&
                rating == review.rating &&
                Objects.equals(comment, review.comment) &&
                Objects.equals(userName, review.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, comment, userName);
    }
}