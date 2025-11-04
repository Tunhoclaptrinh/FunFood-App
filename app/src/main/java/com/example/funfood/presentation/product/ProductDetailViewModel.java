package com.example.funfood.presentation.product;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.funfood.data.repository.ProductRepository;
import com.example.funfood.domain.model.Product;
import com.example.funfood.util.Resource;

public class ProductDetailViewModel extends AndroidViewModel {

    private final ProductRepository productRepository;
    private final MutableLiveData<Resource<Product>> productLiveData = new MutableLiveData<>();

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
    }

    public LiveData<Resource<Product>> getProductLiveData() {
        return productLiveData;
    }

    public void loadProduct(int productId) {
        productLiveData.setValue(Resource.loading(null));
        productRepository.getProductById(productId).observeForever(resource -> {
            productLiveData.setValue(resource);
        });
    }
}