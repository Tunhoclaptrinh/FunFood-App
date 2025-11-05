package com.example.funfood.presentation.checkout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.repository.AddressRepository;
import com.example.funfood.data.repository.CartRepository;
import com.example.funfood.data.repository.OrderRepository;
import com.example.funfood.data.repository.PromotionRepository;
import com.example.funfood.domain.model.Address;
import com.example.funfood.domain.model.Cart;
import com.example.funfood.domain.model.Order;
import com.example.funfood.util.Resource;

import java.util.List;

public class CheckoutViewModel extends AndroidViewModel {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final PromotionRepository promotionRepository;

    private final MutableLiveData<Resource<Cart>> cartLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Address>>> addressesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<Order>> orderResult = new MutableLiveData<>();

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
        orderRepository = new OrderRepository(application);
        addressRepository = new AddressRepository(application);
        promotionRepository = new PromotionRepository(application);
    }

    public LiveData<Resource<Cart>> getCartLiveData() {
        return cartLiveData;
    }

    public LiveData<Resource<List<Address>>> getAddressesLiveData() {
        return addressesLiveData;
    }

    public LiveData<Resource<Order>> getOrderResult() {
        return orderResult;
    }

    /**
     * Lấy giỏ hàng hiện tại
     */
    public void loadCart() {
        cartRepository.getCart().observeForever(resource -> {
            cartLiveData.setValue(resource);
        });
    }

    /**
     * Lấy danh sách địa chỉ
     */
    public void loadAddresses() {
        addressRepository.getAddresses().observeForever(resource -> {
            addressesLiveData.setValue(resource);
        });
    }

    /**
     * Tạo đơn hàng mới
     */
    public void createOrder(OrderRepository.CreateOrderRequest request) {
        orderRepository.createOrder(request).observeForever(resource -> {
            orderResult.setValue(resource);

            if (resource.getStatus() == Resource.Status.SUCCESS) {
                // Clear cart after successful order
                clearCart();
            }
        });
    }

    /**
     * Xóa giỏ hàng
     */
    private void clearCart() {
        cartRepository.clearCart().observeForever(resource -> {
            // Cart cleared, no action needed
        });
    }

    /**
     * Validate promotion code
     */
    public void validatePromotion(String code, int orderValue, int deliveryFee) {
        // This would typically call an API to validate the promotion
        // For now, we'll just show a message
        // You can implement this when backend endpoint is available
    }
}