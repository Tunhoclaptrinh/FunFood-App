package com.example.funfood.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.CategoryApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Category;
import com.example.funfood.util.Resource;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private final CategoryApi categoryApi;

    public CategoryRepository(Context context) {
        this.categoryApi = RetrofitClient.getInstance(context).createService(CategoryApi.class);
    }

    public LiveData<Resource<List<Category>>> getCategories() {
        MutableLiveData<Resource<List<Category>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        categoryApi.getCategories().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Category>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to load categories", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }
}