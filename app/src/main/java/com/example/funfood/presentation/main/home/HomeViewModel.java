package com.example.funfood.presentation.main.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.repository.CategoryRepository;
import com.example.funfood.data.repository.ProductRepository;
import com.example.funfood.data.repository.PromotionRepository;
import com.example.funfood.data.repository.RestaurantRepository;
import com.example.funfood.domain.model.Category;
import com.example.funfood.domain.model.Product;
import com.example.funfood.domain.model.Promotion;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.util.Resource;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;
    private final PromotionRepository promotionRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;

    // LiveData
    private final MutableLiveData<Resource<List<Category>>> categoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Promotion>>> promotionsLiveData = new MutableLiveData<>();
    private final MediatorLiveData<Resource<List<Restaurant>>> restaurantsLiveData = new MediatorLiveData<>();
    private final MutableLiveData<Resource<List<Product>>> productsLiveData = new MutableLiveData<>();

    // Filter state
    private final MutableLiveData<Integer> selectedCategoryId = new MutableLiveData<>();
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>();

    // Pagination
    private int currentPage = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private final int pageSize = 10;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        promotionRepository = new PromotionRepository(application);
        restaurantRepository = new RestaurantRepository(application);
        productRepository = new ProductRepository(application);

        // Auto load featured products on init
        loadFeaturedProducts();
    }

    // Getters
    public LiveData<Resource<List<Category>>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public LiveData<Resource<List<Promotion>>> getPromotionsLiveData() {
        return promotionsLiveData;
    }

    public LiveData<Resource<List<Restaurant>>> getRestaurantsLiveData() {
        return restaurantsLiveData;
    }

    public LiveData<Resource<List<Product>>> getProductsLiveData() {
        return productsLiveData;
    }

    public LiveData<Integer> getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    // Load categories
    public void loadCategories() {
        categoryRepository.getCategories().observeForever(resource -> {
            categoriesLiveData.setValue(resource);
        });
    }

    // Load promotions
    public void loadPromotions() {
        promotionRepository.getActivePromotions().observeForever(resource -> {
            promotionsLiveData.setValue(resource);
        });
    }

    // Load restaurants with pagination
    public void loadRestaurants(int page) {
        if (isLoading || (isLastPage && page > 1)) {
            return;
        }

        currentPage = page;
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

    // Load more restaurants (for infinite scroll)
    public void loadMoreRestaurants() {
        if (!isLastPage && !isLoading) {
            loadRestaurants(currentPage + 1);
        }
    }

    // Refresh restaurants
    public void refreshRestaurants() {
        currentPage = 1;
        isLastPage = false;
        isLoading = false;
        loadRestaurants(currentPage);
    }

    // Search restaurants
    public void searchRestaurants(String query) {
        searchQuery.setValue(query);

        if (query == null || query.trim().isEmpty()) {
            refreshRestaurants();
            return;
        }

        restaurantRepository.searchRestaurants(query).observeForever(resource -> {
            restaurantsLiveData.setValue(resource);
        });
    }

    // Filter by category (load products by category)
    public void filterByCategory(int categoryId) {
        selectedCategoryId.setValue(categoryId);
        loadProductsByCategory(categoryId);
    }

    // Clear category filter
    public void clearCategoryFilter() {
        selectedCategoryId.setValue(null);
        refreshRestaurants();
    }

    // Load products by category
    private void loadProductsByCategory(int categoryId) {
        // This will load products from all restaurants in this category
        productRepository.getProducts(1, 50).observeForever(resource -> {
            if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
                if (resource.getData() != null) {
                    // Filter products by category
                    List<Product> filteredProducts = new java.util.ArrayList<>();
                    for (Product product : resource.getData()) {
                        if (product.getCategoryId() == categoryId) {
                            filteredProducts.add(product);
                        }
                    }
                    productsLiveData.setValue(Resource.success(filteredProducts));
                }
            } else {
                productsLiveData.setValue(resource);
            }
        });
    }

    // Load featured products (for home screen)
    public void loadFeaturedProducts() {
        productRepository.getProducts(1, 10).observeForever(resource -> {
            productsLiveData.setValue(resource);
        });
    }

    // Pagination helpers
    public boolean canLoadMore() {
        return !isLastPage && !isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    // Reset all filters and pagination
    public void resetFilters() {
        selectedCategoryId.setValue(null);
        searchQuery.setValue(null);
        currentPage = 1;
        isLastPage = false;
        isLoading = false;
        refreshRestaurants();
    }
}