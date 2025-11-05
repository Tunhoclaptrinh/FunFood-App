package com.example.funfood.presentation.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.funfood.databinding.ActivityCartBinding;
import com.example.funfood.domain.model.Cart;
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

        // Setup RecyclerView
        setupRecyclerView();

        // Buttons
        binding.btnCheckout.setOnClickListener(v -> proceedToCheckout());
        binding.btnContinueShopping.setOnClickListener(v -> finish());

        // Load cart
        viewModel.loadCart();
    }

    private void setupRecyclerView() {
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
            // Không cần làm gì khi SUCCESS, vì getCartLiveData sẽ tự động trigger và cập nhật
        });

        viewModel.getUpdateResult().observe(this, resource -> {
            if (resource == null) return;

            if (resource.getStatus() == Resource.Status.ERROR) {
                handleError(resource.getMessage());
                // Nếu lỗi, load lại giỏ hàng để reset
                viewModel.loadCart();
            }
        });
    }

    private void displayCart(Cart cart) {
        Cart.CartSummary summary = cart.getSummary();

        if (summary == null || summary.getTotalItems() == 0 || cart.getItems() == null || cart.getItems().isEmpty()) {
            showEmptyCart();
        } else {
            showCartContent(cart, summary);
        }
    }

    private void showCartContent(Cart cart, Cart.CartSummary summary) {
        binding.layoutEmpty.setVisibility(View.GONE);
        binding.layoutCartContent.setVisibility(View.VISIBLE);

        // Set items
        if (cart.getItems() != null) {
            adapter.setItems(cart.getItems());
        }

        // Update summary
        binding.tvSubtotal.setText(CurrencyUtil.formatCurrency(summary.getSubtotal()));
        binding.tvDeliveryFee.setText(CurrencyUtil.formatCurrency(summary.getDeliveryFee()));
        binding.tvTotal.setText(CurrencyUtil.formatCurrency(summary.getTotal()));

        // Enable checkout button
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
        // Load lại giỏ hàng khi quay lại từ màn hình Checkout (nếu đặt hàng thành công)
        viewModel.loadCart();
    }
}