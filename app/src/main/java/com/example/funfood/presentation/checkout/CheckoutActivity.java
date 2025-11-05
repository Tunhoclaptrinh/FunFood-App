package com.example.funfood.presentation.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView; // FIX: Import AutoCompleteTextView

import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.databinding.ActivityCheckoutBinding;
import com.example.funfood.data.repository.OrderRepository;
// FIX: Import lớp CartResponse mới
import com.example.funfood.data.remote.dto.response.CartResponse;
// FIX: Xóa import Cart cũ
// import com.example.funfood.domain.model.Cart;
import com.example.funfood.domain.model.Address;
import com.example.funfood.presentation.address.AddressListActivity;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.order.OrderDetailActivity;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.Resource;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends BaseActivity<ActivityCheckoutBinding> {

    private CheckoutViewModel viewModel;
    // FIX 1: Thay đổi kiểu của biến thành viên
    private CartResponse currentCart;
    private Address selectedAddress;
    private String selectedPaymentMethod = "cash";

    // Mảng này cần được truy cập bởi listener
    private String[] paymentValues = {"cash", "card", "momo", "zalopay"};

    @Override
    protected ActivityCheckoutBinding getViewBinding() {
        return ActivityCheckoutBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Thanh toán");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Payment method spinner
        setupPaymentMethodSpinner();

        // Select address
        binding.btnSelectAddress.setOnClickListener(v -> {
            // Chuyển người dùng đến màn hình quản lý/sửa địa chỉ
            Intent intent = new Intent(this, AddressListActivity.class);
            startActivity(intent);
        });

        // Promotion code
        binding.btnApplyPromotion.setOnClickListener(v -> applyPromotion());

        // Place order
        binding.btnPlaceOrder.setOnClickListener(v -> placeOrder());

        // Load cart data
        viewModel.loadCart();
    }

    private void setupPaymentMethodSpinner() {
        String[] paymentMethods = {"Tiền mặt", "Thẻ tín dụng", "Momo", "ZaloPay"};
        // paymentValues đã được chuyển lên biến thành viên

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, paymentMethods);

        // FIX: Casting binding tới AutoCompleteTextView (vì nó nằm trong TextInputLayout)
        ((AutoCompleteTextView) binding.spinnerPaymentMethod).setAdapter(adapter);

        // FIX: Đặt giá trị mặc định và listener cho AutoCompleteTextView
        ((AutoCompleteTextView) binding.spinnerPaymentMethod).setText(paymentMethods[0], false);
        selectedPaymentMethod = paymentValues[0];

        ((AutoCompleteTextView) binding.spinnerPaymentMethod).setOnItemClickListener(
                (parent, view, position, id) -> {
                    selectedPaymentMethod = paymentValues[position];
                });
    }

    @Override
    protected void observeData() {
        // FIX 2: Observer giờ sẽ nhận Resource<CartResponse>
        viewModel.getCartLiveData().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;
                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        // Đây là dòng 106, giờ đã hợp lệ!
                        currentCart = resource.getData();
                        displayCartSummary(resource.getData());

                        // Tải địa chỉ sau khi tải giỏ hàng thành công
                        viewModel.loadAddresses();
                    }
                    break;
                case ERROR:
                    hideLoading();
                    handleError(resource.getMessage());
                    break;
            }
        });

        // Addresses
        viewModel.getAddressesLiveData().observe(this, resource -> {
            if (resource == null) return;

            if (resource.getStatus() == Resource.Status.SUCCESS) {
                if (resource.getData() != null && !resource.getData().isEmpty()) {
                    // Theo yêu cầu mới: Luôn lấy địa chỉ đầu tiên (vì chỉ có 1)
                    selectedAddress = resource.getData().get(0);
                    binding.tvSelectedAddress.setText(selectedAddress.getAddress());
                } else {
                    // Xử lý trường hợp người dùng chưa có địa chỉ nào
                    binding.tvSelectedAddress.setText("Vui lòng thêm địa chỉ");
                    showToast("Vui lòng thêm địa chỉ giao hàng");
                }
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                handleError("Không thể tải danh sách địa chỉ: " + resource.getMessage());
            }
        });

        // Order Result
        viewModel.getOrderResult().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    binding.btnPlaceOrder.setEnabled(false);
                    showToast("Đang xử lý...");
                    break;

                case SUCCESS:
                    binding.btnPlaceOrder.setEnabled(true);
                    showToast("✓ Đặt hàng thành công!");
                    // Navigate to order detail or home
                    navigateToOrderSuccess(resource.getData().getId());
                    break;

                case ERROR:
                    binding.btnPlaceOrder.setEnabled(true);
                    handleError(resource.getMessage());
                    break;
            }
        });
    }

    // FIX 3: Thay đổi tham số của hàm
    private void displayCartSummary(CartResponse cart) {
        if (cart.getSummary() != null) {
            // FIX 4: Sử dụng CartSummary từ CartResponse
            CartResponse.CartSummary summary = cart.getSummary();
            binding.tvSubtotal.setText(CurrencyUtil.formatCurrency(summary.getSubtotal()));
            binding.tvDeliveryFee.setText(CurrencyUtil.formatCurrency(summary.getDeliveryFee()));

            // Tính toán giảm giá
            double discount = summary.getSubtotal() + summary.getDeliveryFee() - summary.getTotal();
            binding.tvDiscount.setText(CurrencyUtil.formatCurrency(-discount)); // Hiển thị số âm

            binding.tvTotal.setText(CurrencyUtil.formatCurrency(summary.getTotal()));
        }
    }

    private void showAddressDialog(List<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            showToast("Không có địa chỉ nào. Vui lòng thêm địa chỉ.");
            // TODO: Chuyển sang màn hình thêm địa chỉ
            return;
        }

        String[] addressLabels = new String[addresses.size()];
        int checkedItem = 0;
        for (int i = 0; i < addresses.size(); i++) {
            addressLabels[i] = addresses.get(i).getLabel() + " - " + addresses.get(i).getAddress();
            if (selectedAddress != null && addresses.get(i).getId() == selectedAddress.getId()) {
                checkedItem = i;
            }
        }


        new MaterialAlertDialogBuilder(this)
                .setTitle("Chọn địa chỉ giao hàng")
                .setSingleChoiceItems(addressLabels, checkedItem, (dialog, which) -> {
                    selectedAddress = addresses.get(which);
                    binding.tvSelectedAddress.setText(selectedAddress.getAddress());
                    dialog.dismiss();
                })
                .show();
    }

    private void applyPromotion() {
        String promoCode = binding.etPromotionCode.getText().toString().trim();
        if (promoCode.isEmpty()) {
            showToast("Vui lòng nhập mã khuyến mãi");
            return;
        }

        // (Phần logic này giờ đã đúng vì currentCart là CartResponse)
        if (currentCart == null || currentCart.getSummary() == null) {
            showToast("Không thể áp dụng mã khi giỏ hàng trống");
            return;
        }

        viewModel.validatePromotion(
                promoCode,
                currentCart.getSummary().getSubtotal(),
                currentCart.getSummary().getDeliveryFee()
        );

        showToast("Đang áp dụng mã (logic chưa hoàn thiện)...");
    }

    private void placeOrder() {
        // Validation
        if (selectedAddress == null) {
            showToast("Vui lòng chọn địa chỉ giao hàng");
            return;
        }

        // (Phần logic này giờ đã đúng vì currentCart là CartResponse)
        if (currentCart == null || currentCart.getItems() == null || currentCart.getItems().isEmpty()) {
            showToast("Giỏ hàng trống");
            return;
        }

        if (!isNetworkAvailable()) {
            showNetworkError();
            return;
        }

        // Get note
        String note = binding.etNote.getText().toString().trim();

        // Get promo code
        String promoCode = binding.etPromotionCode.getText().toString().trim();

        // Get first restaurant from cart items
        // (Phần logic này giờ đã đúng vì currentCart là CartResponse)
        if (currentCart.getItems().get(0).getRestaurant() == null) {
            handleError("Lỗi dữ liệu giỏ hàng (thiếu thông tin nhà hàng)");
            return;
        }
        int restaurantId = currentCart.getItems().get(0).getRestaurant().getId();

        // Create order request
        List<OrderRepository.OrderItem> orderItems = new ArrayList<>();
        // (Đây là CartItem, không phải CartResponse, nên nó đúng)
        for (com.example.funfood.domain.model.CartItem item : currentCart.getItems()) {
            orderItems.add(new OrderRepository.OrderItem(item.getProductId(), item.getQuantity()));
        }

        OrderRepository.CreateOrderRequest request = new OrderRepository.CreateOrderRequest(
                restaurantId,
                orderItems,
                selectedAddress.getAddress(),
                selectedAddress.getLatitude(),
                selectedAddress.getLongitude(),
                selectedPaymentMethod,
                note,
                promoCode.isEmpty() ? null : promoCode
        );

        viewModel.createOrder(request);
    }

    private void navigateToOrderSuccess(int orderId) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("order_id", orderId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Kết thúc CheckoutActivity
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.scrollView.setVisibility(View.GONE);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Luôn tải lại địa chỉ khi quay lại màn hình này.
        viewModel.loadAddresses();

        // Bạn cũng có thể load lại cart nếu cần (CartActivity đã làm điều này)
        viewModel.loadCart();
    }
}