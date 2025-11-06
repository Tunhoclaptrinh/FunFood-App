package com.example.funfood.presentation.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.funfood.databinding.ActivityCartBinding;
// FIX: Import CartResponse mới
import com.example.funfood.data.remote.dto.response.CartResponse;
// FIX: Xóa import Cart cũ
// import com.example.funfood.domain.model.Cart;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.checkout.CheckoutActivity;
import com.example.funfood.presentation.cart.adapter.CartAdapter;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.Resource;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CartActivity extends BaseActivity<ActivityCartBinding> {

    private CartViewModel viewModel;
    private CartAdapter adapter;

    @Override
    protected ActivityCartBinding getViewBinding() {
        return ActivityCartBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Giỏ hàng");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // FIX: BƯỚC 1: Luôn luôn cài đặt RecyclerView TRƯỚC
        // (Sửa lỗi NullPointerException)
        setupRecyclerView();

        // Buttons
        binding.btnCheckout.setOnClickListener(v -> proceedToCheckout());
        binding.btnContinueShopping.setOnClickListener(v -> finish());

        // FIX: BƯỚC 2: XÓA loadCart() ở đây.
        // onResume() sẽ gọi, tránh bị double-load
        // viewModel.loadCart();
    }
    private void setupRecyclerView() {
        // Adapter được khởi tạo ở đây (adapter không còn null)
        adapter = new CartAdapter();
        adapter.setOnQuantityChangeListener((cartItemId, newQuantity) -> {
            if (newQuantity <= 0) {
                // Hiển thị dialog xác nhận xóa
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Xóa sản phẩm?")
                        .setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng?")
                        .setPositiveButton("Xóa", (dialog, which) -> viewModel.removeFromCart(cartItemId))
                        .setNegativeButton("Hủy", (dialog, which) -> adapter.notifyDataSetChanged()) // Reset lại UI
                        .show();
            } else {
                viewModel.updateQuantity(cartItemId, newQuantity);
            }
        });

        adapter.setOnDeleteListener(cartItemId -> {
            // Hiển thị dialog xác nhận xóa
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Xóa sản phẩm?")
                    .setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng?")
                    .setPositiveButton("Xóa", (dialog, which) -> viewModel.removeFromCart(cartItemId))
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCartItems.setAdapter(adapter);
    }

    @Override
    protected void observeData() {
        // FIX: Thay đổi Observer để nhận CartResponse
        viewModel.getCartLiveData().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;

                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        displayCart(resource.getData());
                    } else {
                        showEmptyCart();
                    }
                    break;

                case ERROR:
                    hideLoading();
                    handleError(resource.getMessage());
                    break;
            }
        });

        viewModel.getRemoveResult().observe(this, resource -> {
            if (resource == null) return;
            if (resource.getStatus() == Resource.Status.LOADING) {
                showToast("Đang xóa...");
            }
            if (resource.getStatus() == Resource.Status.ERROR) {
                handleError(resource.getMessage());
            }
        });

        viewModel.getUpdateResult().observe(this, resource -> {
            if (resource == null) return;
            if (resource.getStatus() == Resource.Status.ERROR) {
                handleError(resource.getMessage());
                viewModel.loadCart();
            }
        });
    }

    // FIX: Thay đổi tham số từ Cart -> CartResponse
    private void displayCart(CartResponse cart) {
        // FIX: Sử dụng CartResponse.CartSummary
        CartResponse.CartSummary summary = cart.getSummary();

        if (summary == null || summary.getTotalItems() == 0 || cart.getItems() == null || cart.getItems().isEmpty()) {
            showEmptyCart();
        } else {
            showCartContent(cart, summary);
        }
    }

    // FIX: Thay đổi tham số từ Cart -> CartResponse
    private void showCartContent(CartResponse cart, CartResponse.CartSummary summary) {
        binding.layoutEmpty.setVisibility(View.GONE);
        binding.layoutCartContent.setVisibility(View.VISIBLE);

        if (cart.getItems() != null) {
            // Dòng này (tương đương dòng 128) giờ đã an toàn
            adapter.setItems(cart.getItems());
        }

        // Các hàm get...() này đến từ lớp CartSummary trong CartResponse
        binding.tvSubtotal.setText(CurrencyUtil.formatCurrency(summary.getSubtotal()));
        binding.tvDeliveryFee.setText(CurrencyUtil.formatCurrency(summary.getDeliveryFee()));
        binding.tvTotal.setText(CurrencyUtil.formatCurrency(summary.getTotal()));

        binding.btnCheckout.setEnabled(true);
    }

    private void showEmptyCart() {
        binding.layoutEmpty.setVisibility(View.VISIBLE);
        binding.layoutCartContent.setVisibility(View.GONE);
        binding.btnCheckout.setEnabled(false);
    }

    private void proceedToCheckout() {
        if (!isNetworkAvailable()) {
            showNetworkError();
            return;
        }

        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.layoutCartContent.setVisibility(View.GONE); // Ẩn cả layout content
        binding.layoutEmpty.setVisibility(View.GONE);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        // Không hiện gì ở đây, để logic displayCart quyết định
    }

    @Override
    protected void onResume() {
        super.onResume();
        // FIX: BƯỚC 3: Chỉ cần gọi loadCart() 1 lần tại đây.
        // Nó sẽ chạy khi vào Activity và khi quay lại (từ Checkout)
        viewModel.loadCart();
    }
}