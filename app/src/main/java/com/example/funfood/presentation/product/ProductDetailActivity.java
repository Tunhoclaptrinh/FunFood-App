package com.example.funfood.presentation.product;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri; // THÊM IMPORT
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.R;
import com.example.funfood.databinding.ActivityProductDetailBinding;
import com.example.funfood.domain.model.Product;
import com.example.funfood.domain.model.Restaurant; // THÊM IMPORT
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.cart.CartActivity;
import com.example.funfood.presentation.cart.CartViewModel;
import com.example.funfood.presentation.restaurant.detail.RestaurantDetailActivity; // THÊM IMPORT
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

                        // HIỂN THỊ THÔNG TIN NHÀ HÀNG (MỚI)
                        if (currentProduct.getRestaurant() != null) {
                            displayRestaurantInfo(currentProduct.getRestaurant());
                        } else {
                            binding.cardRestaurantInfo.setVisibility(View.GONE);
                        }
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
            binding.btnAddToCart.setText("Thêm vào giỏ hàng"); // Đã xóa số lượng (quantity) khỏi nút
            binding.btnAddToCart.setEnabled(true);
        }
    }

    /**
     * Hiển thị thông tin nhà hàng và gán sự kiện click (MỚI)
     */
    private void displayRestaurantInfo(Restaurant restaurant) {
        binding.cardRestaurantInfo.setVisibility(View.VISIBLE);

        binding.tvRestaurantName.setText(restaurant.getName());
        binding.tvRestaurantAddress.setText(restaurant.getAddress());
        binding.tvRestaurantPhone.setText(restaurant.getPhone());

        // Click để chuyển sang chi tiết nhà hàng
        binding.layoutRestaurantInfo.setOnClickListener(v -> {
            openRestaurantDetail(restaurant.getId());
        });

        // Click để gọi điện
        binding.layoutRestaurantPhone.setOnClickListener(v -> {
            dialPhoneNumber(restaurant.getPhone());
        });
    }

    /**
     * Mở màn hình chi tiết nhà hàng (MỚI)
     */
    private void openRestaurantDetail(int restaurantId) {
        // Giả sử bạn có một Activity tên là RestaurantDetailActivity
        // và bạn dùng Constants.KEY_RESTAURANT_ID để truyền ID
        Intent intent = new Intent(this, RestaurantDetailActivity.class);
        intent.putExtra(Constants.KEY_RESTAURANT_ID, restaurantId);
        startActivity(intent);
    }

    /**
     * Mở trình quay số điện thoại (MỚI)
     */
    private void dialPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            showToast("Nhà hàng không cung cấp số điện thoại");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }


    /**
     * Hiển thị dialog chọn số lượng
     */
    private void showQuantityDialog() {
        if (currentProduct == null) return;

        if (!currentProduct.isAvailable()) {
            showToast("Sản phẩm đã hết hàng");
            return;
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_quantity_picker, null);
        android.widget.NumberPicker numberPicker = dialogView.findViewById(R.id.number_picker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99); // Giới hạn số lượng
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // "Thổi" menu của bạn (menu_main.xml) vào Toolbar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Kiểm tra đúng ID 'action_cart' từ file XML của bạn
        if (item.getItemId() == R.id.action_cart) {
            // Mở CartActivity
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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