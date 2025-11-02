package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Promotion;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PromotionApi {
    @GET("promotions/active")
    Call<ApiResponse<List<Promotion>>> getActivePromotions();
}