package com.example.funfood.presentation.checkout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer; // <<< QUAN TRỌNG: Import

import com.example.funfood.data.remote.dto.request.CreateOrderRequest;
import com.example.funfood.data.remote.dto.response.CartResponse; // <<< FIX: Import mới
import com.example.funfood.data.repository.AddressRepository;
import com.example.funfood.data.repository.CartRepository;
import com.example.funfood.data.repository.OrderRepository;
import com.example.funfood.data.repository.PromotionRepository;
import com.example.funfood.domain.model.Address;
// import com.example.funfood.domain.model.Cart; // <<< FIX: Xóa import cũ
import com.example.funfood.domain.model.Order;
import com.example.funfood.util.Resource;

import java.util.List;

public class CheckoutViewModel extends AndroidViewModel {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final PromotionRepository promotionRepository;

    // FIX 1: Thay đổi kiểu dữ liệu
    private final MutableLiveData<Resource<CartResponse>> cartLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Address>>> addressesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Resource<Order>> orderResult = new MutableLiveData<>();

    public CheckoutViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
        orderRepository = new OrderRepository(application);
        addressRepository = new AddressRepository(application);
        promotionRepository = new PromotionRepository(application);
    }

    // FIX 2: Thay đổi kiểu trả về
    public LiveData<Resource<CartResponse>> getCartLiveData() {
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
        // FIX 3: Sửa lỗi biên dịch VÀ fix memory leak
        LiveData<Resource<CartResponse>> repoLiveData = cartRepository.getCart();
        repoLiveData.observeForever(new Observer<Resource<CartResponse>>() {
            @Override
            public void onChanged(Resource<CartResponse> resource) {
                // Đây là dòng 57
                cartLiveData.setValue(resource);

                // Tự hủy observer
                if (resource.getStatus() != Resource.Status.LOADING) {
                    repoLiveData.removeObserver(this);
                }
            }
        });
    }

    /**
     * Lấy danh sách địa chỉ
     */
    public void loadAddresses() {
        // FIX 4: Sửa memory leak
        LiveData<Resource<List<Address>>> repoLiveData = addressRepository.getAddresses();
        repoLiveData.observeForever(new Observer<Resource<List<Address>>>() {
            @Override
            public void onChanged(Resource<List<Address>> resource) {
                addressesLiveData.setValue(resource);
                // Tự hủy observer
                if (resource.getStatus() != Resource.Status.LOADING) {
                    repoLiveData.removeObserver(this);
                }
            }
        });
    }

    /**
     * Tạo đơn hàng mới
     */
    public void createOrder(CreateOrderRequest request) {
        orderResult.setValue(Resource.loading(null));

        // FIX 5: Sửa memory leak
        LiveData<Resource<Order>> repoLiveData = orderRepository.createOrder(request);
        repoLiveData.observeForever(new Observer<Resource<Order>>() {
            @Override
            public void onChanged(Resource<Order> resource) {
                orderResult.setValue(resource);

                if (resource.getStatus() == Resource.Status.SUCCESS) {
                    clearCart(); // Xóa giỏ hàng sau khi đặt thành công
                }

                // Tự hủy observer
                if (resource.getStatus() != Resource.Status.LOADING) {
                    repoLiveData.removeObserver(this);
                }
            }
        });
    }

    /**
     * Xóa giỏ hàng
     */
    private void clearCart() {
        // FIX 6: Sửa memory leak
        LiveData<Resource<Void>> repoLiveData = cartRepository.clearCart();
        repoLiveData.observeForever(new Observer<Resource<Void>>() {
            @Override
            public void onChanged(Resource<Void> resource) {
                // Đã xóa, không cần làm gì
                // Tự hủy observer
                if (resource.getStatus() != Resource.Status.LOADING) {
                    repoLiveData.removeObserver(this);
                }
            }
        });
    }

    /**
     * Validate promotion code
     */
    public void validatePromotion(String code, double orderValue, double deliveryFee) {
        // ... (Giữ nguyên logic của bạn)
    }
}