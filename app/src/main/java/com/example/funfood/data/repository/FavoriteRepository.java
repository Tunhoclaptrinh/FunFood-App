package com.example.funfood.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.remote.api.FavoriteApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Favorite;
import com.example.funfood.util.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteRepository {

    private final FavoriteApi favoriteApi;

    public FavoriteRepository(FavoriteApi favoriteApi) {
        this.favoriteApi = favoriteApi;
    }

    public LiveData<Resource<List<Favorite>>> getFavorites(int page, int limit) {
        MutableLiveData<Resource<List<Favorite>>> data = new MutableLiveData<>();
        // SỬA 1: Gọi loading(null) để tránh NullPointerException
        data.setValue(Resource.loading(null));

        favoriteApi.getFavorites(page, limit, "restaurant").enqueue(new Callback<ApiResponse<List<Favorite>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Favorite>>> call, Response<ApiResponse<List<Favorite>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    data.setValue(Resource.success(response.body().getData()));
                } else {
                    // SỬA 2: Nối mã lỗi vào message và truyền null cho data
                    String errorMessage = "Không thể tải danh sách. Mã lỗi: " + response.code();
                    data.setValue(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Favorite>>> call, Throwable t) {
                // SỬA 3: Nối lỗi vào message và truyền null cho data
                String errorMessage = "Lỗi kết nối: " + t.getMessage();
                data.setValue(Resource.error(errorMessage, null));
            }
        });
        return data;
    }

    public interface OperationCallback {
        void onResult(Resource<Object> resource);
    }

    public void toggleFavorite(int restaurantId, OperationCallback callback) {
        // SỬA 4: Phải truyền null cho data
        callback.onResult(Resource.loading(null));

        favoriteApi.toggleFavorite(restaurantId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResult(Resource.success(response.body().getData()));
                } else {
                    // SỬA 5: Nối mã lỗi vào message và truyền null cho data
                    String errorMessage = "Thao tác thất bại. Mã lỗi: " + response.code();
                    callback.onResult(Resource.error(errorMessage, null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                // SỬA 6: Nối lỗi vào message và truyền null cho data
                String errorMessage = "Lỗi kết nối: " + t.getMessage();
                callback.onResult(Resource.error(errorMessage, null));
            }
        });
    }
}