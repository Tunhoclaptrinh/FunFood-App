package com.example.funfood.presentation.notification;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.NotificationApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.repository.NotificationRepository;
import com.example.funfood.domain.model.Notification;
import com.example.funfood.util.Resource;
import com.example.funfood.util.SingleEvent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends AndroidViewModel {

    private final NotificationRepository repository;

    private final MutableLiveData<Resource<List<Notification>>> _notifications = new MutableLiveData<>();
    public final LiveData<Resource<List<Notification>>> notifications = _notifications;

    private final MutableLiveData<SingleEvent<String>> _toastEvent = new MutableLiveData<>();
    public final LiveData<SingleEvent<String>> toastEvent = _toastEvent;

    private int currentPage = 1;
    private static final int PAGE_LIMIT = 20;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        NotificationApi api = RetrofitClient.getInstance(application).createService(NotificationApi.class);
        this.repository = new NotificationRepository(api);
        fetchNotifications(false);
    }

    public void fetchNotifications(boolean isRefresh) {
        if (isRefresh) {
            currentPage = 1;
        }

        Resource<List<Notification>> currentResource = _notifications.getValue();
        List<Notification> currentData = isRefresh ? null : (currentResource != null ? currentResource.getData() : null);
        _notifications.setValue(Resource.loading(currentData));

        repository.getNotifications(currentPage, PAGE_LIMIT).enqueue(new Callback<ApiResponse<List<Notification>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Notification> newData = response.body().getData();
                    if (newData == null) newData = new ArrayList<>();

                    List<Notification> currentList = isRefresh ? new ArrayList<>() :
                            (currentData != null ? new ArrayList<>(currentData) : new ArrayList<>());
                    currentList.addAll(newData);

                    _notifications.setValue(Resource.success(currentList));
                    currentPage++;
                } else {
                    _notifications.setValue(Resource.error("Không thể tải thông báo", currentData));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                _notifications.setValue(Resource.error(t.getMessage(), currentData));
            }
        });
    }

    public void markAsRead(Notification notification) {
        if (notification.isRead()) return;

        repository.markAsRead(notification.getId()).enqueue(new Callback<ApiResponse<Notification>>() {
            @Override
            public void onResponse(Call<ApiResponse<Notification>> call, Response<ApiResponse<Notification>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
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
                    fetchNotifications(true);
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

    private void updateNotificationInList(Notification updatedNotification) {
        Resource<List<Notification>> currentResource = _notifications.getValue();
        if (currentResource == null || currentResource.getData() == null) return;

        List<Notification> newList = new ArrayList<>(currentResource.getData());
        for (int i = 0; i < newList.size(); i++) {
            if (newList.get(i).getId() == updatedNotification.getId()) {
                newList.set(i, updatedNotification);
                break;
            }
        }
        _notifications.setValue(Resource.success(newList));
    }

    private void removeNotificationFromList(Notification notificationToRemove) {
        Resource<List<Notification>> currentResource = _notifications.getValue();
        if (currentResource == null || currentResource.getData() == null) return;

        List<Notification> newList = new ArrayList<>();
        for (Notification n : currentResource.getData()) {
            if (n.getId() != notificationToRemove.getId()) {
                newList.add(n);
            }
        }
        _notifications.setValue(Resource.success(newList));
    }
}