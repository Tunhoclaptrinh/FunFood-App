package com.example.funfood.data.repository;

import com.example.funfood.data.remote.api.NotificationApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Notification;
import java.util.List;
import retrofit2.Call;

public class NotificationRepository {

    private final NotificationApi notificationApi;

    public NotificationRepository(NotificationApi notificationApi) {
        this.notificationApi = notificationApi;
    }

    public Call<ApiResponse<List<Notification>>> getNotifications(int page, int limit) {
        return notificationApi.getNotifications(page, limit);
    }

    public Call<ApiResponse<Notification>> markAsRead(int notificationId) {
        return notificationApi.markAsRead(notificationId);
    }

    public Call<ApiResponse<Object>> markAllAsRead() {
        return notificationApi.markAllAsRead();
    }

    public Call<ApiResponse<Object>> deleteNotification(int notificationId) {
        return notificationApi.deleteNotification(notificationId);
    }

    public Call<ApiResponse<Object>> clearAllNotifications() {
        return notificationApi.clearAllNotifications();
    }
}