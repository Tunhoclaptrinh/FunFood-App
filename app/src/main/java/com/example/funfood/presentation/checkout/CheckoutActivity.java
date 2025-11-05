package com.example.funfood.presentation.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.databinding.ActivityCheckoutBinding;
import com.example.funfood.data.repository.OrderRepository;
import com.example.funfood.domain.model.Cart;
import com.example.funfood.domain.model.Address;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.util.CurrencyUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends BaseActivity<ActivityCheckoutBinding> {

    private CheckoutViewModel viewModel;
    private Cart currentCart;
    private Address selectedAddress;
    private String selectedPaymentMethod = "cash";

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
        binding.btnSelectAddress.setOnClickListener(v -> viewModel.loadAddresses());

        // Promotion code
        binding.btnApplyPromotion.setOnClickListener(v -> applyPromotion());

        // Place order
        binding.btnPlaceOrder.setOnClickListener(v -> placeOrder());

        // Load cart data
        viewModel.loadCart();
    }

    private void setupPaymentMethodSpinner() {
        String[] paymentMethods = {"Tiền mặt", "Thẻ tín dụng", "Momo", "ZaloPay"};
        String[] paymentValues = {"cash", "card", "momo", "zalopay"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPaymentMethod.setAdapter(adapter);

        binding.spinnerPaymentMethod.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, View view,
                                               int position, long id) {
                        selectedPaymentMethod = paymentValues[position];
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {}
                });
    }

    @Override
    protected void observeData() {
        // Cart Data
        viewModel.getCartLiveData().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;
                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        currentCart = resource.getData();
                        displayCartSummary(resource.getData());
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

            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                showAddressDialog(resource.getData());
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

    private void displayCartSummary(Cart cart) {
        if (cart.getSummary() != null) {
            Cart.CartSummary summary = cart.getSummary();
            binding.tvSubtotal.setText(CurrencyUtil.formatCurrency(summary.getSubtotal()));
            binding.tvDeliveryFee.setText(CurrencyUtil.formatCurrency(summary.getDeliveryFee()));
            binding.tvDiscount.setText(CurrencyUtil.formatCurrency(summary.getTotal() -
                    summary.getSubtotal() - summary.getDeliveryFee()));
            binding.tvTotal.setText(CurrencyUtil.formatCurrency(summary.getTotal()));
        }
    }

    private void showAddressDialog(List<Address> addresses) {
        String[] addressLabels = new String[addresses.size()];
        for (int i = 0; i < addresses.size(); i++) {
            addressLabels[i] = addresses.get(i).getLabel() + " - " + addresses.get(i).getAddress();
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("Chọn địa chỉ giao hàng")
                .setSingleChoiceItems(addressLabels, 0, (dialog, which) -> {
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

        viewModel.validatePromotion(promoCode);
    }

    private void placeOrder() {
        // Validation
        if (selectedAddress == null) {
            showToast("Vui lòng chọn địa chỉ giao hàng");
            return;
        }

        if (currentCart == null || currentCart.getItems().isEmpty()) {
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
        int restaurantId = currentCart.getItems().get(0).getRestaurant().getId();

        // Create order request
        List<OrderRepository.OrderItem> orderItems = new ArrayList<>();
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
}
