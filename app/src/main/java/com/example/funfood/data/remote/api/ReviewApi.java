package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.request.CreateReviewRequest;
import com.example.funfood.data.remote.dto.response.ReviewResponse; // Giả sử bạn có DTO này
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewApi {

    @GET("reviews/restaurant/{restaurantId}")
    Call<ApiResponse<List<ReviewResponse>>> getReviewsByRestaurant(
            @Path("restaurantId") String restaurantId,
            @Query("_page") int page,
            @Query("_limit") int limit
    );

    @GET("reviews/user/me")
    Call<ApiResponse<List<ReviewResponse>>> getMyReviews();

    @POST("reviews")
    Call<ApiResponse<ReviewResponse>> createReview(@Body CreateReviewRequest request);

    @PUT("reviews/{id}")
    Call<ApiResponse<ReviewResponse>> updateReview(@Path("id") String reviewId, @Body CreateReviewRequest request);
}