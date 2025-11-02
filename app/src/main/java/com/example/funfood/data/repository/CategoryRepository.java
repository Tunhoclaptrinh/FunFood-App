package com.example.funfood.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.CategoryApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.response.CategoryResponse;
import com.example.funfood.domain.model.Category;
import com.example.funfood.util.Resource;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    private final CategoryApi categoryApi;

    public CategoryRepository(Context context) {
        this.categoryApi = RetrofitClient.getInstance(context).createService(CategoryApi.class);
    }

    private Category convertDtoToModel(CategoryResponse dto) {
        return new Category(String.valueOf(dto.getId()), dto.getName(), dto.getImage());
    }

    public LiveData<Resource<List<Category>>> getCategories() {
        MutableLiveData<Resource<List<Category>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        categoryApi.getCategories().enqueue(new Callback<ApiResponse<List<CategoryResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CategoryResponse>>> call, Response<ApiResponse<List<CategoryResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Category> modelList = new ArrayList<>();
                    // Kiểm tra null cho getData()
                    if (response.body().getData() != null) {
                        for (CategoryResponse dto : response.body().getData()) {
                            modelList.add(convertDtoToModel(dto));
                        }
                    }
                    data.setValue(Resource.success(modelList));
                } else {
                    data.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CategoryResponse>>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return data;
    }

}