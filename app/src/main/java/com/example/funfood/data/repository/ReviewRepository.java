package com.example.funfood.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.ReviewApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.request.CreateReviewRequest;
import com.example.funfood.data.remote.dto.response.ReviewResponse;
import com.example.funfood.domain.model.Review;
import com.example.funfood.util.Resource;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {

    private final ReviewApi reviewApi;

    public ReviewRepository(Context context) {
        this.reviewApi = RetrofitClient.getInstance(context).createService(ReviewApi.class);
    }

    // Hàm chuyển đổi DTO -> Model
    private Review convertDtoToModel(ReviewResponse dto) {
        return new Review(
                dto.getId(),
                dto.getRating(),
                dto.getComment(),
                dto.getCreatedAt(),
                dto.getUser() != null ? dto.getUser().getName() : "Người dùng ẩn",
                dto.getUser() != null ? dto.getUser().getAvatar() : null
        );
    }

    public LiveData<Resource<List<Review>>> getReviewsByRestaurant(String restaurantId, int page, int limit) {
        MutableLiveData<Resource<List<Review>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        reviewApi.getReviewsByRestaurant(restaurantId, page, limit).enqueue(new Callback<ApiResponse<List<ReviewResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ReviewResponse>>> call, Response<ApiResponse<List<ReviewResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Review> modelList = new ArrayList<>();
                    if (response.body().getData() != null) {
                        for (ReviewResponse dto : response.body().getData()) {
                            modelList.add(convertDtoToModel(dto));
                        }
                    }
                    data.setValue(Resource.success(modelList));
                } else {
                    data.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ReviewResponse>>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

    public LiveData<Resource<Review>> createReview(int restaurantId, int orderId, int rating, String comment) {
        MutableLiveData<Resource<Review>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        CreateReviewRequest request = new CreateReviewRequest(restaurantId, orderId, rating, comment);

        reviewApi.createReview(request).enqueue(new Callback<ApiResponse<ReviewResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReviewResponse>> call, Response<ApiResponse<ReviewResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    data.setValue(Resource.success(convertDtoToModel(response.body().getData())));
                } else {
                    data.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ReviewResponse>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }
}