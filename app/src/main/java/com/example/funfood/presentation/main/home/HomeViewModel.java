package com.example.funfood.presentation.main.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.repository.CategoryRepository;
import com.example.funfood.data.repository.PromotionRepository;
import com.example.funfood.data.repository.RestaurantRepository;
import com.example.funfood.domain.model.Category;
import com.example.funfood.domain.model.Promotion;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.util.Resource;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;
    private final PromotionRepository promotionRepository;
    private final RestaurantRepository restaurantRepository;

    private final MutableLiveData<Resource<List<Category>>> categoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Promotion>>> promotionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Restaurant>>> restaurantsLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        promotionRepository = new PromotionRepository(application);
        restaurantRepository = new RestaurantRepository(application);
    }

    public LiveData<Resource<List<Category>>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public LiveData<Resource<List<Promotion>>> getPromotionsLiveData() {
        return promotionsLiveData;
    }

    public LiveData<Resource<List<Restaurant>>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }

    public void loadCategories() {
        categoryRepository.getCategories().observeForever(resource -> {
            categoriesLiveData.setValue(resource);
        });
    }

    public void loadPromotions() {
        promotionRepository.getActivePromotions().observeForever(resource -> {
            promotionsLiveData.setValue(resource);
        });
    }

    public void loadRestaurants(int page) {
        restaurantRepository.getRestaurants(page, 10).observeForever(resource -> {
            restaurantsLiveData.setValue(resource);
        });
    }

    public void searchRestaurants(String query) {
        restaurantRepository.searchRestaurants(query).observeForever(resource -> {
            restaurantsLiveData.setValue(resource);
        });
    }
}