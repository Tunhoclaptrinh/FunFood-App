package com.example.funfood.presentation.restaurant.detail;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.funfood.databinding.ActivityRestaurantDetailBinding;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.restaurant.detail.adapter.ProductAdapter;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.ImageUtil;

public class RestaurantDetailActivity extends BaseActivity<ActivityRestaurantDetailBinding> {

    private RestaurantDetailViewModel viewModel;
    private ProductAdapter productAdapter;
    private int restaurantId;

    @Override
    protected ActivityRestaurantDetailBinding getViewBinding() {
        return ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // Get restaurant ID from intent
        restaurantId = getIntent().getIntExtra("RESTAURANT_ID", -1);
        if (restaurantId == -1) {
            showToast("Invalid restaurant");
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Setup products RecyclerView
        setupProductsRecyclerView();

        // Load data
        viewModel.loadRestaurant(restaurantId);
        viewModel.loadProducts(restaurantId);
    }

    @Override
    protected void observeData() {
        // Restaurant details
        viewModel.getRestaurantLiveData().observe(this, resource -> {
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

        // Products
        viewModel.getProductsLiveData().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    binding.progressProducts.setVisibility(View.VISIBLE);
                    break;

                case SUCCESS:
                    binding.progressProducts.setVisibility(View.GONE);
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        productAdapter.setItems(resource.getData());
                        binding.rvProducts.setVisibility(View.VISIBLE);
                        binding.tvEmptyProducts.setVisibility(View.GONE);
                    } else {
                        binding.rvProducts.setVisibility(View.GONE);
                        binding.tvEmptyProducts.setVisibility(View.VISIBLE);
                    }
                    break;

                case ERROR:
                    binding.progressProducts.setVisibility(View.GONE);
                    showToast("Không thể tải menu: " + resource.getMessage());
                    break;
            }
        });
    }

    private void setupProductsRecyclerView() {
        productAdapter = new ProductAdapter();
        productAdapter.setOnItemClickListener((product, position) -> {
            // TODO: Show product detail dialog or navigate
            showToast("Sản phẩm: " + product.getName());
        });

        binding.rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvProducts.setAdapter(productAdapter);
    }

    private void displayRestaurantInfo(com.example.funfood.domain.model.Restaurant restaurant) {
        // Image
        ImageUtil.loadImage(this, restaurant.getImage(), binding.ivRestaurant);

        // Name
        binding.tvName.setText(restaurant.getName());
        binding.collapsingToolbar.setTitle(restaurant.getName());

        // Description
        if (restaurant.getDescription() != null && !restaurant.getDescription().isEmpty()) {
            binding.tvDescription.setText(restaurant.getDescription());
            binding.tvDescription.setVisibility(View.VISIBLE);
        } else {
            binding.tvDescription.setVisibility(View.GONE);
        }

        // Rating
        binding.tvRating.setText(String.format("%.1f", restaurant.getRating()));
        binding.tvTotalReviews.setText(String.format("(%d đánh giá)", restaurant.getTotalReviews()));

        // Delivery info
        binding.tvDeliveryTime.setText(restaurant.getDeliveryTime());
        binding.tvDeliveryFee.setText(CurrencyUtil.formatCurrency(restaurant.getDeliveryFee()));

        // Address
        if (restaurant.getAddress() != null) {
            binding.tvAddress.setText(restaurant.getAddress());
            binding.layoutAddress.setVisibility(View.VISIBLE);
        } else {
            binding.layoutAddress.setVisibility(View.GONE);
        }

        // Phone
        if (restaurant.getPhone() != null) {
            binding.tvPhone.setText(restaurant.getPhone());
            binding.layoutPhone.setVisibility(View.VISIBLE);
            binding.layoutPhone.setOnClickListener(v -> {
                // TODO: Make phone call
                showToast("Gọi: " + restaurant.getPhone());
            });
        } else {
            binding.layoutPhone.setVisibility(View.GONE);
        }

        // Open/Close status
        if (restaurant.isOpen()) {
            binding.tvStatus.setText("Đang mở cửa");
            binding.tvStatus.setTextColor(getColor(com.example.funfood.R.color.success));
            binding.tvOpenTime.setText(String.format("%s - %s",
                    restaurant.getOpenTime(), restaurant.getCloseTime()));
        } else {
            binding.tvStatus.setText("Đã đóng cửa");
            binding.tvStatus.setTextColor(getColor(com.example.funfood.R.color.error));
            binding.tvOpenTime.setText("Mở cửa lúc " + restaurant.getOpenTime());
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