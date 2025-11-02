package com.example.funfood.presentation.restaurant.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.repository.ProductRepository;
import com.example.funfood.data.repository.RestaurantRepository;
import com.example.funfood.domain.model.Product;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.util.Resource;

import java.util.List;

public class RestaurantDetailViewModel extends AndroidViewModel {

    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;

    private final MutableLiveData<Resource<Restaurant>> restaurantLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Product>>> productsLiveData = new MutableLiveData<>();

    public RestaurantDetailViewModel(@NonNull Application application) {
        super(application);
        restaurantRepository = new RestaurantRepository(application);
        productRepository = new ProductRepository(application);
    }

    public LiveData<Resource<Restaurant>> getRestaurantLiveData() {
        return restaurantLiveData;
    }

    public LiveData<Resource<List<Product>>> getProductsLiveData() {
        return productsLiveData;
    }

    public void loadRestaurant(int restaurantId) {
        restaurantRepository.getRestaurantById(restaurantId).observeForever(resource -> {
            restaurantLiveData.setValue(resource);
        });
    }

    public void loadProducts(int restaurantId) {
        productRepository.getRestaurantProducts(restaurantId).observeForever(resource -> {
            productsLiveData.setValue(resource);
        });
    }
}