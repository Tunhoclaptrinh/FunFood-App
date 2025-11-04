package com.example.funfood.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.ProductApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Product;
import com.example.funfood.util.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private final ProductApi productApi;

    public ProductRepository(Context context) {
        this.productApi = RetrofitClient.getInstance(context).createService(ProductApi.class);
    }

    public LiveData<Resource<List<Product>>> getRestaurantProducts(int restaurantId) {
        MutableLiveData<Resource<List<Product>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        productApi.getRestaurantProducts(restaurantId, true).enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Product>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to load products", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<Product>> getProductById(int productId) {
        MutableLiveData<Resource<Product>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        productApi.getProductById(productId).enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Product> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to load product", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Product>>> getProducts(int page, int limit) {
        MutableLiveData<Resource<List<Product>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        productApi.getProducts(page, limit).enqueue(new Callback<ApiResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Product>>> call, Response<ApiResponse<List<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Product>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to load products", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Product>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }
}