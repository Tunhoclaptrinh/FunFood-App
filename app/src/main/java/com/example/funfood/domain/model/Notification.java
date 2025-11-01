package com.example.funfood.domain.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Notification {

    @SerializedName("id")
    private int id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type; // 'order', 'promotion', 'favorite', 'system'

    @SerializedName("refId")
    private int refId;

    @SerializedName("isRead")
    private boolean isRead;

    @SerializedName("createdAt")
    private Date createdAt;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getRefId() { return refId; }
    public void setRefId(int refId) { this.refId = refId; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id == that.id && isRead == that.isRead && title.equals(that.title) && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, title, message, isRead);
    }
}