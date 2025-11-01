package com.example.funfood.presentation.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.repository.NotificationRepository;
import com.example.funfood.domain.model.Notification;
import com.example.funfood.util.Resource;
import com.example.funfood.util.SingleEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class NotificationViewModel extends ViewModel {

    private final NotificationRepository repository;

    private final MutableLiveData<Resource<List<Notification>>> _notifications = new MutableLiveData<>();
    public final LiveData<Resource<List<Notification>>> notifications = _notifications;

    private final MutableLiveData<SingleEvent<String>> _toastEvent = new MutableLiveData<>();
    public final LiveData<SingleEvent<String>> toastEvent = _toastEvent;

    private int currentPage = 1;
    private static final int PAGE_LIMIT = 20;

    @Inject
    public NotificationViewModel(NotificationRepository repository) {
        this.repository = repository;
        fetchNotifications(false); // Tải lần đầu khi ViewModel được tạo
    }

    public void fetchNotifications(boolean isRefresh) {
        if (isRefresh) {
            currentPage = 1;
        }
        _notifications.setValue(Resource.loading(isRefresh ? null : _notifications.getValue().data));

        repository.getNotifications(currentPage, PAGE_LIMIT).enqueue(new Callback<ApiResponse<List<Notification>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Notification> currentList = isRefresh ? new ArrayList<>() : (_notifications.getValue().data != null ? _notifications.getValue().data : new ArrayList<>());
                    currentList.addAll(response.body().getData());
                    _notifications.setValue(Resource.success(currentList));
                    // Có thể check thêm logic "hasMore" nếu API hỗ trợ
                    currentPage++;
                } else {
                    _notifications.setValue(Resource.error("Không thể tải thông báo", _notifications.getValue().data));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                _notifications.setValue(Resource.error(t.getMessage(), _notifications.getValue().data));
            }
        });
    }

    public void markAsRead(Notification notification) {
        if (notification.isRead()) return; // Đã đọc rồi thì không cần gọi API

        repository.markAsRead(notification.getId()).enqueue(new Callback<ApiResponse<Notification>>() {
            @Override
            public void onResponse(Call<ApiResponse<Notification>> call, Response<ApiResponse<Notification>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Cập nhật lại item trong danh sách LiveData
                    updateNotificationInList(response.body().getData());
                } else {
                    _toastEvent.setValue(new SingleEvent<>("Đã có lỗi xảy ra"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Notification>> call, Throwable t) {
                _toastEvent.setValue(new SingleEvent<>(t.getMessage()));
            }
        });
    }

    public void deleteNotification(Notification notification) {
        repository.deleteNotification(notification.getId()).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    removeNotificationFromList(notification);
                    _toastEvent.setValue(new SingleEvent<>("Đã xóa thông báo"));
                } else {
                    _toastEvent.setValue(new SingleEvent<>("Không thể xóa thông báo"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                _toastEvent.setValue(new SingleEvent<>(t.getMessage()));
            }
        });
    }

    public void markAllAsRead() {
        repository.markAllAsRead().enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    _toastEvent.setValue(new SingleEvent<>("Đã đánh dấu đọc tất cả"));
                    fetchNotifications(true); // Tải lại toàn bộ danh sách
                } else {
                    _toastEvent.setValue(new SingleEvent<>("Không thể đánh dấu đọc tất cả"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                _toastEvent.setValue(new SingleEvent<>(t.getMessage()));
            }
        });
    }

    // --- Helper functions ---

    private void updateNotificationInList(Notification updatedNotification) {
        List<Notification> currentList = _notifications.getValue() != null ? _notifications.getValue().data : null;
        if (currentList == null) return;

        List<Notification> newList = new ArrayList<>(currentList);
        for (int i = 0; i < newList.size(); i++) {
            if (newList.get(i).getId() == updatedNotification.getId()) {
                newList.set(i, updatedNotification);
                break;
            }
        }
        _notifications.setValue(Resource.success(newList));
    }

    private void removeNotificationFromList(Notification notificationToRemove) {
        List<Notification> currentList = _notifications.getValue() != null ? _notifications.getValue().data : null;
        if (currentList == null) return;

        List<Notification> newList = new ArrayList<>(currentList);
        newList.removeIf(n -> n.getId() == notificationToRemove.getId());
        _notifications.setValue(Resource.success(newList));
    }
}