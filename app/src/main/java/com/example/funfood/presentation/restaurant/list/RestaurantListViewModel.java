package com.example.funfood.presentation.restaurant.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.repository.RestaurantRepository;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.util.Resource;

import java.util.List;

public class RestaurantListViewModel extends AndroidViewModel {

    private final RestaurantRepository restaurantRepository;
    private final MutableLiveData<Resource<List<Restaurant>>> restaurantsLiveData = new MutableLiveData<>();

    // Pagination
    private int currentPage = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private final int pageSize = 10;
    private int currentCategoryId = -1;

    public RestaurantListViewModel(@NonNull Application application) {
        super(application);
        restaurantRepository = new RestaurantRepository(application);
    }

    public LiveData<Resource<List<Restaurant>>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Load all restaurants with pagination
     */
    public void loadRestaurants(int page) {
        if (isLoading || (isLastPage && page > 1)) {
            return;
        }

        currentPage = page;
        currentCategoryId = -1;
        isLoading = true;

        restaurantRepository.getRestaurants(page, pageSize).observeForever(resource -> {
            isLoading = false;

            if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
                if (resource.getData() != null) {
                    isLastPage = resource.getData().size() < pageSize;
                }
            }

            restaurantsLiveData.setValue(resource);
        });
    }

    /**
     * Load restaurants by category with pagination
     * Note: Backend doesn't support category filter for restaurants yet
     * So we'll load all and filter on client side
     */
    public void loadRestaurantsByCategory(int categoryId, int page) {
        if (isLoading || (isLastPage && page > 1)) {
            return;
        }

        currentPage = page;
        currentCategoryId = categoryId;
        isLoading = true;

        // Load all restaurants and filter
        restaurantRepository.getRestaurants(page, 50).observeForever(resource -> {
            isLoading = false;

            if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
                if (resource.getData() != null) {
                    // Filter by category
                    List<Restaurant> filteredRestaurants = new java.util.ArrayList<>();
                    for (Restaurant restaurant : resource.getData()) {
                        if (restaurant.getCategoryId() == categoryId) {
                            filteredRestaurants.add(restaurant);
                        }
                    }

                    isLastPage = filteredRestaurants.size() < pageSize;
                    restaurantsLiveData.setValue(Resource.success(filteredRestaurants));
                } else {
                    restaurantsLiveData.setValue(resource);
                }
            } else {
                restaurantsLiveData.setValue(resource);
            }
        });
    }

    /**
     * Load more restaurants
     */
    public void loadMoreRestaurants() {
        if (!isLastPage && !isLoading) {
            if (currentCategoryId > 0) {
                loadRestaurantsByCategory(currentCategoryId, currentPage + 1);
            } else {
                loadRestaurants(currentPage + 1);
            }
        }
    }

    /**
     * Refresh restaurants
     */
    public void refreshRestaurants(int categoryId) {
        currentPage = 1;
        isLastPage = false;
        isLoading = false;

        if (categoryId > 0) {
            loadRestaurantsByCategory(categoryId, currentPage);
        } else {
            loadRestaurants(currentPage);
        }
    }

    /**
     * Check if can load more
     */
    public boolean canLoadMore() {
        return !isLastPage && !isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }
}