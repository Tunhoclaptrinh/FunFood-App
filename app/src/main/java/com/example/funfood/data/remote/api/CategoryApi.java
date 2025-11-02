package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.response.CategoryResponse; // Giả sử bạn có DTO này
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryApi {

    @GET("categories")
    Call<ApiResponse<List<CategoryResponse>>> getCategories();

    @GET("categories/{id}")
    Call<ApiResponse<CategoryResponse>> getCategoryDetails(@Path("id") String categoryId);
}