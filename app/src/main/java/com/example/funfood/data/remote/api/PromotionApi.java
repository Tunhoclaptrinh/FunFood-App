package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.request.ValidatePromotionRequest;
import com.example.funfood.data.remote.dto.response.PromotionResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PromotionApi {

    @GET("promotions/active")
    Call<ApiResponse<List<PromotionResponse>>> getActivePromotions();

    @GET("promotions/code/{code}")
    Call<ApiResponse<PromotionResponse>> getPromotionByCode(@Path("code") String code);

    @POST("promotions/validate")
    Call<ApiResponse<Object>> validatePromotion(@Body ValidatePromotionRequest request);
}