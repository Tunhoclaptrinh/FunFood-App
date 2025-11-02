package com.example.funfood.presentation.main.home;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.example.funfood.domain.model.Category;
import com.example.funfood.domain.model.Promotion;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.data.repository.CategoryRepository;
import com.example.funfood.data.repository.PromotionRepository;
import com.example.funfood.data.repository.RestaurantRepository;
import com.example.funfood.util.Resource;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;
    private final PromotionRepository promotionRepository;
    private final RestaurantRepository restaurantRepository;

    private final MediatorLiveData<Resource<List<Category>>> _categories = new MediatorLiveData<>();
    public final LiveData<Resource<List<Category>>> categories = _categories;

    private final MediatorLiveData<Resource<List<Promotion>>> _promotions = new MediatorLiveData<>();
    public final LiveData<Resource<List<Promotion>>> promotions = _promotions;

    private final MediatorLiveData<Resource<List<Restaurant>>> _restaurants = new MediatorLiveData<>();
    public final LiveData<Resource<List<Restaurant>>> restaurants = _restaurants;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        promotionRepository = new PromotionRepository(application); // Cần tạo class này
        restaurantRepository = new RestaurantRepository(application); // Cần tạo class này

        fetchData();
    }

    public void fetchData() {
        fetchCategories();
        fetchPromotions();
        fetchRestaurants();
    }

    private void fetchCategories() {
        _categories.setValue(Resource.loading(null));
        _categories.addSource(categoryRepository.getCategories(), resource -> {
            _categories.setValue(resource);
        });
    }

    private void fetchPromotions() {
        _promotions.setValue(Resource.loading(null));
        // Giả sử PromotionRepository có hàm getActivePromotions()
        _promotions.addSource(promotionRepository.getActivePromotions(), resource -> {
            _promotions.setValue(resource);
        });
    }

    private void fetchRestaurants() {
        _restaurants.setValue(Resource.loading(null));
        // Giả sử RestaurantRepository có hàm getRestaurants()
        _restaurants.addSource(restaurantRepository.getRestaurants(1, 10, null, true, 4.0, null), resource -> {
            _restaurants.setValue(resource);
        });
    }
}