package com.example.funfood.presentation.product;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import androidx.lifecycle.ViewModelProvider;
import com.example.funfood.R;
import com.example.funfood.databinding.ActivityProductDetailBinding;
import com.example.funfood.domain.model.Product;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.util.Constants;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.ImageUtil;

public class ProductDetailActivity extends BaseActivity<ActivityProductDetailBinding> {

    private ProductDetailViewModel viewModel;
    private int productId;

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

        // Cài đặt Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Nút Thêm vào giỏ
        binding.btnAddToCart.setOnClickListener(v -> {
            // TODO: Triển khai logic thêm vào giỏ hàng
            showToast("Đã thêm vào giỏ hàng!");
        });

        // Tải dữ liệu
        viewModel.loadProduct(productId);
    }

    @Override
    protected void observeData() {
        viewModel.getProductLiveData().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;
                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        displayProductInfo(resource.getData());
                    }
                    break;
                case ERROR:
                    hideLoading();
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

        // Tình trạng (Hết hàng/Còn hàng)
        if (!product.isAvailable()) {
            binding.btnAddToCart.setText("Hết hàng");
            binding.btnAddToCart.setEnabled(false);
        } else {
            binding.btnAddToCart.setText("Thêm vào giỏ hàng");
            binding.btnAddToCart.setEnabled(true);
        }
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