package com.example.funfood.presentation.product.list;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.repository.ProductRepository;
import com.example.funfood.domain.model.Product;
import com.example.funfood.util.Resource;
import java.util.ArrayList;
import java.util.List;

public class ProductListViewModel extends AndroidViewModel {

    private final ProductRepository productRepository;
    private final MutableLiveData<Resource<List<Product>>> productsLiveData = new MutableLiveData<>();
    private final List<Product> productList = new ArrayList<>();

    private int currentPage = 1;
    private int totalPages = 1;
    private boolean isLoading = false;
    private static final int PAGE_LIMIT = 10;

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
    }

    public LiveData<Resource<List<Product>>> getProductsLiveData() {
        return productsLiveData;
    }

    public void loadProducts() {
        // Nếu đang tải hoặc đã tải hết, không làm gì cả
        if (isLoading || currentPage > totalPages) {
            return;
        }

        isLoading = true;
        // Hiển thị loading cho trang đầu, hoặc loading-more cho trang sau
        if (currentPage == 1) {
            productsLiveData.setValue(Resource.loading(null));
        }

        productRepository.getProducts(currentPage, PAGE_LIMIT).observeForever(apiResponseResource -> {
            if (apiResponseResource == null) return;

            // Lấy thông tin phân trang từ API
            ApiResponse<List<Product>> apiResponse = apiResponseResource.getApiResponse();
            if (apiResponse != null && apiResponse.getPagination() != null) {
                this.totalPages = apiResponse.getPagination().getTotalPages();
            }

            if (apiResponseResource.getStatus() == Resource.Status.SUCCESS && apiResponse != null) {
                List<Product> newProducts = apiResponse.getData();
                if (newProducts != null && !newProducts.isEmpty()) {
                    productList.addAll(newProducts);
                    productsLiveData.setValue(Resource.success(productList, apiResponse));
                    currentPage++; // Tăng trang cho lần gọi tiếp theo
                } else {
                    // Trường hợp trang đầu tiên không có gì
                    if(currentPage == 1) {
                        productsLiveData.setValue(Resource.success(new ArrayList<>(), apiResponse));
                    }
                }
            } else if (apiResponseResource.getStatus() == Resource.Status.ERROR) {
                productsLiveData.setValue(Resource.error(apiResponseResource.getMessage(), productList));
            }
            isLoading = false;
        });
    }
}