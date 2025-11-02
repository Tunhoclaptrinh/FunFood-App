package com.example.funfood.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.RestaurantApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.response.RestaurantResponse;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.util.Resource;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {

    private final RestaurantApi restaurantApi;

    public RestaurantRepository(Context context) {
        this.restaurantApi = RetrofitClient.getInstance(context).createService(RestaurantApi.class);
    }

    // Hàm chuyển đổi DTO -> Model
    private Restaurant convertDtoToModel(RestaurantResponse dto) {
        return new Restaurant(
                dto.getId(),
                dto.getName(),
                dto.getAddress(),
                dto.getRating(),
                dto.getImageUrl(),
                dto.getDistance() // Giả sử DTO có distance
        );
    }

    public LiveData<Resource<List<Restaurant>>> getRestaurants(
            int page, int limit, String categoryId, Boolean isOpen, Double ratingGte, String searchTerm
    ) {
        MutableLiveData<Resource<List<Restaurant>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        restaurantApi.getRestaurants(page, limit, categoryId, isOpen, ratingGte, searchTerm)
                .enqueue(new Callback<ApiResponse<List<RestaurantResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<RestaurantResponse>>> call, Response<ApiResponse<List<RestaurantResponse>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<Restaurant> modelList = new ArrayList<>();
                            if (response.body().getData() != null) {
                                for (RestaurantResponse dto : response.body().getData()) {
                                    modelList.add(convertDtoToModel(dto));
                                }
                            }
                            data.setValue(Resource.success(modelList));
                        } else {
                            data.setValue(Resource.error(response.message(), null));
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<RestaurantResponse>>> call, Throwable t) {
                        data.setValue(Resource.error(t.getMessage(), null));
                    }
                });
        return data;
    }
}