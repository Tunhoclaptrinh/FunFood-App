package com.example.funfood.presentation.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.repository.CartRepository;
import com.example.funfood.domain.model.Cart;
import com.example.funfood.domain.model.CartItem;
import com.example.funfood.util.Resource;

public class CartViewModel extends AndroidViewModel {

    private final CartRepository cartRepository;
    private final MutableLiveData<Resource<Cart>> cartLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<CartItem>> addToCartResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<Void>> removeResult = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
        this.cartRepository = new CartRepository(application);
        loadCart();
    }

    public LiveData<Resource<Cart>> getCartLiveData() {
        return cartLiveData;
    }

    public LiveData<Resource<CartItem>> getAddToCartResult() {
        return addToCartResult;
    }

    public LiveData<Resource<Void>> getRemoveResult() {
        return removeResult;
    }

    /**
     * Lấy giỏ hàng
     */
    public void loadCart() {
        cartRepository.getCart().observeForever(resource -> {
            cartLiveData.setValue(resource);
        });
    }

    /**
     * Thêm sản phẩm vào giỏ
     */
    public void addToCart(int productId, int quantity) {
        addToCartResult.setValue(Resource.loading(null));

        cartRepository.addToCart(productId, quantity).observeForever(resource -> {
            addToCartResult.setValue(resource);
            if (resource.getStatus() == Resource.Status.SUCCESS) {
                // Reload cart after successful add
                loadCart();
            }
        });
    }

    /**
     * Cập nhật số lượng
     */
    public void updateQuantity(int cartItemId, int newQuantity) {
        cartRepository.updateCartItem(cartItemId, newQuantity).observeForever(resource -> {
            if (resource.getStatus() == Resource.Status.SUCCESS) {
                loadCart();
            }
        });
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
    public void removeFromCart(int cartItemId) {
        removeResult.setValue(Resource.loading(null));

        cartRepository.removeFromCart(cartItemId).observeForever(resource -> {
            removeResult.setValue(resource);
            if (resource.getStatus() == Resource.Status.SUCCESS) {
                loadCart();
            }
        });
    }

    /**
     * Xóa giỏ hàng
     */
    public void clearCart() {
        cartRepository.clearCart().observeForever(resource -> {
            if (resource.getStatus() == Resource.Status.SUCCESS) {
                loadCart();
            }
        });
    }
}
