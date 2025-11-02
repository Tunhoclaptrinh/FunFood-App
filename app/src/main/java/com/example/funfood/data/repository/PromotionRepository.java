package com.example.funfood.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.PromotionApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Promotion;
import com.example.funfood.util.Resource;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionRepository {
    private final PromotionApi promotionApi;

    public PromotionRepository(Context context) {
        this.promotionApi = RetrofitClient.getInstance(context).createService(PromotionApi.class);
    }

    public LiveData<Resource<List<Promotion>>> getActivePromotions() {
        MutableLiveData<Resource<List<Promotion>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        promotionApi.getActivePromotions().enqueue(new Callback<ApiResponse<List<Promotion>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Promotion>>> call, Response<ApiResponse<List<Promotion>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Promotion>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to load promotions", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Promotion>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }
}