package com.example.funfood.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.CategoryApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Category; // Dùng model
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

    // Hàm giả lập chuyển đổi DTO -> Model
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
                    for (CategoryResponse dto : response.body().getData()) {
                        modelList.add(convertDtoToModel(dto)); // Cần hàm convert
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

    // Cần 1 DTO CategoryResponse.java
    static class CategoryResponse {
        private int id;
        private String name;
        private String image;
        public int getId() { return id; }
        public String getName() { return name; }
        public String getImage() { return image; }
    }
}