package com.example.funfood.presentation.product;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.repository.ProductRepository;
import com.example.funfood.domain.model.Product;
import com.example.funfood.util.Resource;

import java.util.List;

public class ProductListViewModel extends AndroidViewModel {

    private final ProductRepository productRepository;
    private final MutableLiveData<Resource<List<Product>>> productsLiveData = new MutableLiveData<>();

    // Pagination
    private int currentPage = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private final int pageSize = 20;
    private int currentCategoryId = -1;

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
    }

    public LiveData<Resource<List<Product>>> getProductsLiveData() {
        return productsLiveData;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Load all products with pagination
     */
    public void loadProducts(int page) {
        if (isLoading || (isLastPage && page > 1)) {
            return;
        }

        currentPage = page;
        currentCategoryId = -1;
        isLoading = true;

        productRepository.getProducts(page, pageSize).observeForever(resource -> {
            isLoading = false;

            if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
                if (resource.getData() != null) {
                    isLastPage = resource.getData().size() < pageSize;
                }
            }

            productsLiveData.setValue(resource);
        });
    }

    /**
     * Load products by category with pagination
     */
    public void loadProductsByCategory(int categoryId, int page) {
        if (isLoading || (isLastPage && page > 1)) {
            return;
        }

        currentPage = page;
        currentCategoryId = categoryId;
        isLoading = true;

        // Load all products and filter by category
        productRepository.getProducts(page, 50).observeForever(resource -> {
            isLoading = false;

            if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
                if (resource.getData() != null) {
                    // Filter by category
                    List<Product> filteredProducts = new java.util.ArrayList<>();
                    for (Product product : resource.getData()) {
                        if (product.getCategoryId() == categoryId) {
                            filteredProducts.add(product);
                        }
                    }

                    isLastPage = filteredProducts.size() < pageSize;
                    productsLiveData.setValue(Resource.success(filteredProducts));
                } else {
                    productsLiveData.setValue(resource);
                }
            } else {
                productsLiveData.setValue(resource);
            }
        });
    }

    /**
     * Load more products
     */
    public void loadMoreProducts() {
        if (!isLastPage && !isLoading) {
            if (currentCategoryId > 0) {
                loadProductsByCategory(currentCategoryId, currentPage + 1);
            } else {
                loadProducts(currentPage + 1);
            }
        }
    }

    /**
     * Refresh products
     */
    public void refreshProducts(int categoryId) {
        currentPage = 1;
        isLastPage = false;
        isLoading = false;

        if (categoryId > 0) {
            loadProductsByCategory(categoryId, currentPage);
        } else {
            loadProducts(currentPage);
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