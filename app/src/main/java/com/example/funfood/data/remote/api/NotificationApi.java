// in: com.example.funfood.data.remote.api.NotificationApi.java
package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Notification;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationApi {

    /**
     * Lấy danh sách thông báo (có phân trang)
     */
    @GET("notifications")
    Call<ApiResponse<List<Notification>>> getNotifications(@Query("_page") int page, @Query("_limit") int limit);

    /**
     * Đánh dấu một thông báo đã đọc
     */
    @PATCH("notifications/{id}/read")
    Call<ApiResponse<Notification>> markAsRead(@Path("id") int notificationId);

    /**
     * Đánh dấu tất cả đã đọc
     */
    @PATCH("notifications/read-all")
    Call<ApiResponse<Object>> markAllAsRead(); // Response có thể là một object count

    /**
     * Xóa một thông báo
     */
    @DELETE("notifications/{id}")
    Call<ApiResponse<Object>> deleteNotification(@Path("id") int notificationId);

    /**
     * Xóa tất cả thông báo
     */
    @DELETE("notifications")
    Call<ApiResponse<Object>> clearAllNotifications();
}