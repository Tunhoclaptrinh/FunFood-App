package com.example.funfood.presentation.restaurant.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.funfood.databinding.ActivityRestaurantDetailBinding;
import com.example.funfood.domain.model.Product;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.restaurant.detail.adapter.ProductAdapter;
import com.example.funfood.presentation.restaurant.detail.adapter.ReviewAdapter;
import com.example.funfood.util.Constants;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.ImageUtil;

public class RestaurantDetailActivity extends BaseActivity<ActivityRestaurantDetailBinding> {

    private RestaurantDetailViewModel viewModel;
    private ProductAdapter productAdapter;
    private ReviewAdapter reviewAdapter;
    private String restaurantId;

    @Override
    protected ActivityRestaurantDetailBinding getViewBinding() {
        return ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // Get restaurant ID from intent
        restaurantId = getIntent().getStringExtra(Constants.KEY_RESTAURANT_ID);
        if (restaurantId == null) {
            showToast("Lỗi: Không tìm thấy thông tin nhà hàng");
            finish();
            return;
        }

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Setup RecyclerViews
        setupProductRecyclerView();
        setupReviewRecyclerView();

        // Load data
        viewModel.loadRestaurantDetails(restaurantId);
    }

    private void setupProductRecyclerView() {
        productAdapter = new ProductAdapter();
        productAdapter.setOnProductClickListener(product -> {
            showToast("Sản phẩm: " + product.getName());
            // TODO: Mở ProductDetailActivity hoặc Bottom Sheet
        });

        binding.rvProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProducts.setAdapter(productAdapter);
        binding.rvProducts.setNestedScrollingEnabled(false);
    }

    private void setupReviewRecyclerView() {
        reviewAdapter = new ReviewAdapter();
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReviews.setAdapter(reviewAdapter);
        binding.rvReviews.setNestedScrollingEnabled(false);
    }

    @Override
    protected void observeData() {
        // Observe restaurant details
        viewModel.getRestaurant().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;

                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        displayRestaurantInfo(resource.getData());
                    }
                    break;

                case ERROR:
                    hideLoading();
                    handleError(resource.getMessage());
                    break;
            }
        });

        // Observe products
        viewModel.getProducts().observe(this, resource -> {
            if (resource != null && resource.getStatus() == com.example.funfood.util.Resource.Status.SUCCESS) {
                if (resource.getData() != null && !resource.getData().isEmpty()) {
                    productAdapter.submitList(resource.getData());
                    binding.tvNoProducts.setVisibility(View.GONE);
                } else {
                    binding.tvNoProducts.setVisibility(View.VISIBLE);
                }
            }
        });

        // Observe reviews
        viewModel.getReviews().observe(this, resource -> {
            if (resource != null && resource.getStatus() == com.example.funfood.util.Resource.Status.SUCCESS) {
                if (resource.getData() != null && !resource.getData().isEmpty()) {
                    reviewAdapter.submitList(resource.getData());
                    binding.tvNoReviews.setVisibility(View.GONE);
                } else {
                    binding.tvNoReviews.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void displayRestaurantInfo(com.example.funfood.domain.model.Restaurant restaurant) {
        // Load image
        ImageUtil.loadImage(this, restaurant.getImageUrl(), binding.ivRestaurantImage);

        // Set basic info
        binding.tvRestaurantName.setText(restaurant.getName());
        binding.tvRestaurantAddress.setText(restaurant.getAddress());
        binding.tvRating.setText(String.valueOf(restaurant.getRating()));

        // Set delivery info
        binding.tvDeliveryTime.setText("30-40 phút"); // TODO: Get from restaurant data
        binding.tvDeliveryFee.setText(CurrencyUtil.formatCurrency(20000)); // TODO: Get from restaurant data

        // Set open status
        boolean isOpen = true; // TODO: Check restaurant opening hours
        binding.tvOpenStatus.setText(isOpen ? "Đang mở cửa" : "Đã đóng cửa");
        binding.tvOpenStatus.setTextColor(getColor(isOpen ?
                com.example.funfood.R.color.success : com.example.funfood.R.color.error));
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