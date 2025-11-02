package com.example.funfood.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.RestaurantApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.util.Resource;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private final RestaurantApi restaurantApi;

    public RestaurantRepository(Context context) {
        this.restaurantApi = RetrofitClient.getInstance(context).createService(RestaurantApi.class);
    }

    public LiveData<Resource<List<Restaurant>>> getRestaurants(int page, int limit) {
        MutableLiveData<Resource<List<Restaurant>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        restaurantApi.getRestaurants(page, limit).enqueue(new Callback<ApiResponse<List<Restaurant>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Restaurant>>> call, Response<ApiResponse<List<Restaurant>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Restaurant>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to load restaurants", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Restaurant>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<Restaurant>> getRestaurantById(int id) {
        MutableLiveData<Resource<Restaurant>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        restaurantApi.getRestaurantById(id).enqueue(new Callback<ApiResponse<Restaurant>>() {
            @Override
            public void onResponse(Call<ApiResponse<Restaurant>> call, Response<ApiResponse<Restaurant>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Restaurant> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to load restaurant", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Restaurant>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Restaurant>>> searchRestaurants(String query) {
        MutableLiveData<Resource<List<Restaurant>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        restaurantApi.searchRestaurants(query).enqueue(new Callback<ApiResponse<List<Restaurant>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Restaurant>>> call, Response<ApiResponse<List<Restaurant>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Restaurant>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Search failed", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Restaurant>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }
}
