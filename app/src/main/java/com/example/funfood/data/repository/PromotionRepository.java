package com.example.funfood.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.PromotionApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.response.PromotionResponse;
import com.example.funfood.domain.model.Promotion;
import com.example.funfood.util.Resource;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionRepository {

    private final PromotionApi promotionApi;

    public PromotionRepository(Context context) {
        this.promotionApi = RetrofitClient.getInstance(context).createService(PromotionApi.class);
    }

    // Hàm chuyển đổi DTO -> Model
    private Promotion convertDtoToModel(PromotionResponse dto) {
        // Model Promotion chỉ có id, imageUrl, description
        return new Promotion(
                String.valueOf(dto.getId()),
                dto.getImageUrl(),
                dto.getDescription()
        );
    }

    public LiveData<Resource<List<Promotion>>> getActivePromotions() {
        MutableLiveData<Resource<List<Promotion>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        promotionApi.getActivePromotions().enqueue(new Callback<ApiResponse<List<PromotionResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PromotionResponse>>> call, Response<ApiResponse<List<PromotionResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Promotion> modelList = new ArrayList<>();
                    if (response.body().getData() != null) {
                        for (PromotionResponse dto : response.body().getData()) {
                            modelList.add(convertDtoToModel(dto));
                        }
                    }
                    data.setValue(Resource.success(modelList));
                } else {
                    data.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PromotionResponse>>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }
}