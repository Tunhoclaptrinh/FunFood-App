package com.example.funfood.presentation.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer; // <<< QUAN TRỌNG: Import

// FIX: Import lớp mới
import com.example.funfood.data.remote.dto.response.CartResponse;
import com.example.funfood.data.repository.CartRepository;
// FIX: Xóa lớp Cart cũ
// import com.example.funfood.domain.model.Cart;
import com.example.funfood.domain.model.CartItem;
import com.example.funfood.util.Resource;

public class CartViewModel extends AndroidViewModel {

    private final CartRepository cartRepository;

    // FIX 1: Thay đổi kiểu dữ liệu của LiveData
    private final MutableLiveData<Resource<CartResponse>> cartLiveData = new MutableLiveData<>();

    private final MutableLiveData<Resource<CartItem>> addToCartResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<Void>> removeResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<CartItem>> updateResult = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
        this.cartRepository = new CartRepository(application);
        // Xóa loadCart() khỏi constructor (để fix lỗi NullPointerException)
    }

    // FIX 2: Thay đổi kiểu trả về của getter
    public LiveData<Resource<CartResponse>> getCartLiveData() {
        return cartLiveData;
    }

    public LiveData<Resource<CartItem>> getAddToCartResult() {
        return addToCartResult;
    }

    public LiveData<Resource<Void>> getRemoveResult() {
        return removeResult;
    }

    public LiveData<Resource<CartItem>> getUpdateResult() {
        return updateResult;
    }

    /**
     * Lấy giỏ hàng
     */
    public void loadCart() {
        cartLiveData.setValue(Resource.loading(null));

        // FIX 3: Cập nhật kiểu dữ liệu
        LiveData<Resource<CartResponse>> repoLiveData = cartRepository.getCart();

        // FIX 4: Sửa lỗi Memory Leak
        repoLiveData.observeForever(new Observer<Resource<CartResponse>>() {
            @Override
            public void onChanged(Resource<CartResponse> resource) {
                cartLiveData.setValue(resource);

                // Tự động hủy observe sau khi nhận được kết quả
                if (resource.getStatus() != Resource.Status.LOADING) {
                    repoLiveData.removeObserver(this);
                }
            }
        });
    }

    /**
     * Thêm sản phẩm vào giỏ
     */
    public void addToCart(int productId, int quantity) {
        addToCartResult.setValue(Resource.loading(null));

        // FIX: Sửa lỗi Memory Leak
        LiveData<Resource<CartItem>> repoLiveData = cartRepository.addToCart(productId, quantity);
        repoLiveData.observeForever(new Observer<Resource<CartItem>>() {
            @Override
            public void onChanged(Resource<CartItem> resource) {
                addToCartResult.setValue(resource);
                if (resource.getStatus() == Resource.Status.SUCCESS) {
                    loadCart();
                }
                if (resource.getStatus() != Resource.Status.LOADING) {
                    repoLiveData.removeObserver(this);
                }
            }
        });
    }

    /**
     * Cập nhật số lượng
     */
    public void updateQuantity(int cartItemId, int newQuantity) {
        updateResult.setValue(Resource.loading(null));

        // FIX: Sửa lỗi Memory Leak
        LiveData<Resource<CartItem>> repoLiveData = cartRepository.updateCartItem(cartItemId, newQuantity);
        repoLiveData.observeForever(new Observer<Resource<CartItem>>() {
            @Override
            public void onChanged(Resource<CartItem> resource) {
                updateResult.setValue(resource);
                if (resource.getStatus() == Resource.Status.SUCCESS) {
                    loadCart();
                }
                if (resource.getStatus() != Resource.Status.LOADING) {
                    repoLiveData.removeObserver(this);
                }
            }
        });
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
    public void removeFromCart(int cartItemId) {
        removeResult.setValue(Resource.loading(null));

        // FIX: Sửa lỗi Memory Leak
        LiveData<Resource<Void>> repoLiveData = cartRepository.removeFromCart(cartItemId);
        repoLiveData.observeForever(new Observer<Resource<Void>>() {
            @Override
            public void onChanged(Resource<Void> resource) {
                removeResult.setValue(resource);
                if (resource.getStatus() == Resource.Status.SUCCESS) {
                    loadCart();
                }
                if (resource.getStatus() != Resource.Status.LOADING) {
                    repoLiveData.removeObserver(this);
                }
            }
        });
    }

    /**
     * Xóa giỏ hàng
     */
    public void clearCart() {
        // FIX: Sửa lỗi Memory Leak
        LiveData<Resource<Void>> repoLiveData = cartRepository.clearCart();
        repoLiveData.observeForever(new Observer<Resource<Void>>() {
            @Override
            public void onChanged(Resource<Void> resource) {
                if (resource.getStatus() == Resource.Status.SUCCESS) {
                    loadCart();
                }
                if (resource.getStatus() != Resource.Status.LOADING) {
                    repoLiveData.removeObserver(this);
                }
            }
        });
    }
}