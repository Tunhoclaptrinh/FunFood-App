package com.example.funfood.presentation.product;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.R;
import com.example.funfood.databinding.ActivityProductDetailBinding;
import com.example.funfood.domain.model.Product;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.cart.CartViewModel;
import com.example.funfood.util.Constants;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.ImageUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProductDetailActivity extends BaseActivity<ActivityProductDetailBinding> {

    private ProductDetailViewModel viewModel;
    private CartViewModel cartViewModel;
    private int productId;
    private int quantity = 1;
    private Product currentProduct;

    @Override
    protected ActivityProductDetailBinding getViewBinding() {
        return ActivityProductDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // Lấy product ID từ Intent
        productId = getIntent().getIntExtra(Constants.KEY_PRODUCT_ID, -1);
        if (productId == -1) {
            showToast("Sản phẩm không hợp lệ");
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Cài đặt Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Nút Thêm vào giỏ
        binding.btnAddToCart.setOnClickListener(v -> showQuantityDialog());

        // Tải dữ liệu
        viewModel.loadProduct(productId);
    }

    @Override
    protected void observeData() {
        // Product Detail
        viewModel.getProductLiveData().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;
                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        currentProduct = resource.getData();
                        displayProductInfo(resource.getData());
                    }
                    break;
                case ERROR:
                    hideLoading();
                    handleError(resource.getMessage());
                    break;
            }
        });

        // Add to Cart Result
        cartViewModel.getAddToCartResult().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showToast("Đang thêm vào giỏ hàng...");
                    break;
                case SUCCESS:
                    showToast("✓ Đã thêm vào giỏ hàng");
                    quantity = 1; // Reset
                    break;
                case ERROR:
                    handleError(resource.getMessage());
                    break;
            }
        });
    }

    private void displayProductInfo(Product product) {
        // Ảnh
        ImageUtil.loadImage(this, product.getImage(), binding.ivProduct);

        // Tên
        binding.tvName.setText(product.getName());
        binding.collapsingToolbar.setTitle(product.getName());

        // Mô tả
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            binding.tvDescription.setText(product.getDescription());
            binding.tvDescription.setVisibility(View.VISIBLE);
        } else {
            binding.tvDescription.setVisibility(View.GONE);
        }

        // Giá và giảm giá
        if (product.hasDiscount()) {
            binding.tvOriginalPrice.setText(CurrencyUtil.formatCurrency(product.getPrice()));
            binding.tvOriginalPrice.setPaintFlags(
                    binding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            binding.tvOriginalPrice.setVisibility(View.VISIBLE);

            binding.tvPrice.setText(CurrencyUtil.formatCurrency(product.getDiscountedPrice()));
            binding.tvDiscount.setText("-" + product.getDiscount() + "%");
            binding.tvDiscount.setVisibility(View.VISIBLE);
        } else {
            binding.tvOriginalPrice.setVisibility(View.GONE);
            binding.tvDiscount.setVisibility(View.GONE);
            binding.tvPrice.setText(CurrencyUtil.formatCurrency(product.getPrice()));
        }

        // Tình trạng
        if (!product.isAvailable()) {
            binding.btnAddToCart.setText("Hết hàng");
            binding.btnAddToCart.setEnabled(false);
        } else {
            binding.btnAddToCart.setText("Thêm vào giỏ hàng (" + quantity + ")");
            binding.btnAddToCart.setEnabled(true);
        }
    }

    /**
     * Hiển thị dialog chọn số lượng
     */
    private void showQuantityDialog() {
        if (currentProduct == null) return;

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quantity_picker, null);
        android.widget.NumberPicker numberPicker = dialogView.findViewById(R.id.number_picker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99);
        numberPicker.setValue(quantity);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Chọn số lượng")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    quantity = numberPicker.getValue();
                    addToCart();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    private void addToCart() {
        if (currentProduct == null) return;

        if (!isNetworkAvailable()) {
            showNetworkError();
            return;
        }

        cartViewModel.addToCart(currentProduct.getId(), quantity);
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.nestedScrollView.setVisibility(View.GONE);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.nestedScrollView.setVisibility(View.VISIBLE);
    }
}
