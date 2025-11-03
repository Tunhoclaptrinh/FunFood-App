// Đổi tên package này cho đúng với cấu trúc của bạn nếu cần
package com.example.funfood.presentation.product;

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
    private int totalPages = 1; // Giả định là 1 trang cho đến khi API trả về
    private boolean isLoading = false;
    private static final int PAGE_LIMIT = 10;

    public ProductListViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
    }

    public LiveData<Resource<List<Product>>> getProductsLiveData() {
        return productsLiveData;
    }

    public void loadMoreProducts() {
        // Nếu đang tải, hoặc đã hết trang
        if (isLoading || (currentPage > totalPages && totalPages > 1)) {
            return;
        }

        isLoading = true;
        // Hiển thị loading (trang 1) hoặc loading-more (trang > 1)
        productsLiveData.setValue(Resource.loading(currentPage == 1 ? null : productList));

        productRepository.getProducts(currentPage, PAGE_LIMIT).observeForever(resource -> {
            if (resource == null) return;

            ApiResponse<List<Product>> apiResponse = resource.getApiResponse();

            if (resource.getStatus() == Resource.Status.SUCCESS) {
                if (apiResponse != null && apiResponse.getPagination() != null) {
                    this.totalPages = apiResponse.getPagination().getTotalPages();
                }

                List<Product> newProducts = resource.getData();
                if (newProducts != null && !newProducts.isEmpty()) {
                    productList.addAll(newProducts);
                    productsLiveData.setValue(Resource.success(productList, apiResponse));
                    currentPage++; // Tăng trang cho lần gọi tiếp theo
                } else {
                    if (currentPage == 1) { // Lần tải đầu tiên mà không có gì
                        productsLiveData.setValue(Resource.success(new ArrayList<>(), apiResponse));
                    }
                    // Nếu không có sản phẩm mới (đã ở trang cuối), chỉ cần dừng loading
                    productsLiveData.setValue(Resource.success(productList, apiResponse));
                }
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                productsLiveData.setValue(Resource.error(resource.getMessage(), productList, apiResponse));
            }
            isLoading = false;
        });
    }
}