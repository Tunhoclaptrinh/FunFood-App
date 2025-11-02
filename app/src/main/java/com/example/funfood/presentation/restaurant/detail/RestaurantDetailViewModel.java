package com.example.funfood.presentation.restaurant.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.funfood.data.repository.RestaurantRepository;
import com.example.funfood.data.repository.ReviewRepository;
import com.example.funfood.domain.model.Product;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.domain.model.Review;
import com.example.funfood.util.Resource;

import java.util.List;

public class RestaurantDetailViewModel extends AndroidViewModel {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    private final MediatorLiveData<Resource<Restaurant>> _restaurant = new MediatorLiveData<>();
    public final LiveData<Resource<Restaurant>> restaurant = _restaurant;

    private final MediatorLiveData<Resource<List<Product>>> _products = new MediatorLiveData<>();
    public final LiveData<Resource<List<Product>>> products = _products;

    private final MediatorLiveData<Resource<List<Review>>> _reviews = new MediatorLiveData<>();
    public final LiveData<Resource<List<Review>>> reviews = _reviews;

    public RestaurantDetailViewModel(@NonNull Application application) {
        super(application);
        restaurantRepository = new RestaurantRepository(application);
        reviewRepository = new ReviewRepository(application);
    }

    public void loadRestaurantDetails(String restaurantId) {
        loadRestaurant(restaurantId);
        loadProducts(restaurantId);
        loadReviews(restaurantId);
    }

    private void loadRestaurant(String restaurantId) {
        _restaurant.setValue(Resource.loading(null));
        // TODO: Implement getRestaurantById in repository
        // For now, use mock data or implement the API call
    }

    private void loadProducts(String restaurantId) {
        _products.setValue(Resource.loading(null));
        // TODO: Implement getProductsByRestaurant in repository
    }

    private void loadReviews(String restaurantId) {
        _reviews.setValue(Resource.loading(null));
        _reviews.addSource(
                reviewRepository.getReviewsByRestaurant(restaurantId, 1, 10),
                resource -> _reviews.setValue(resource)
        );
    }

    public LiveData<Resource<Restaurant>> getRestaurant() {
        return restaurant;
    }

    public LiveData<Resource<List<Product>>> getProducts() {
        return products;
    }

    public LiveData<Resource<List<Review>>> getReviews() {
        return reviews;
    }
}