package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Category;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {
    @GET("categories")
    Call<ApiResponse<List<Category>>> getCategories();
}