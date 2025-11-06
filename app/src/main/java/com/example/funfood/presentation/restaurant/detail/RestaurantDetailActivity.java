package com.example.funfood.presentation.restaurant.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.funfood.R;
import com.example.funfood.databinding.ActivityRestaurantDetailBinding;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.product.ProductDetailActivity;
import com.example.funfood.presentation.restaurant.detail.adapter.ProductAdapter;
import com.example.funfood.presentation.cart.CartActivity;
import com.example.funfood.util.Constants;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.ImageUtil;

import androidx.lifecycle.ViewModel;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.FavoriteApi;
import com.example.funfood.data.repository.FavoriteRepository;
import com.example.funfood.presentation.main.favorite.FavoriteViewModel;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.example.funfood.R;
import com.example.funfood.presentation.cart.CartActivity;

import android.net.Uri;

public class RestaurantDetailActivity extends BaseActivity<ActivityRestaurantDetailBinding> {

    private RestaurantDetailViewModel viewModel;
    private FavoriteViewModel favoriteViewModel;
    private ProductAdapter productAdapter;
    private int restaurantId;

    // Factory cho FavoriteViewModel
    private static class FavoriteViewModelFactory implements ViewModelProvider.Factory {
        private final FavoriteRepository repository;

        public FavoriteViewModelFactory(FavoriteRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(FavoriteViewModel.class)) {
                //noinspection unchecked
                return (T) new FavoriteViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    @Override
    protected ActivityRestaurantDetailBinding getViewBinding() {
        return ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // Get restaurant ID from intent
        restaurantId = getIntent().getIntExtra(Constants.KEY_RESTAURANT_ID, -1);
        if (restaurantId == -1) {
            showToast("Invalid restaurant");
            finish();
            return;
        }

        // Initialize ViewModels
        viewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);

        // Initialize FavoriteViewModel
        RetrofitClient retrofitClient = RetrofitClient.getInstance(getApplicationContext());
        FavoriteApi favoriteApi = retrofitClient.createService(FavoriteApi.class);
        FavoriteRepository favoriteRepository = new FavoriteRepository(favoriteApi);
        FavoriteViewModelFactory factory = new FavoriteViewModelFactory(favoriteRepository);
        favoriteViewModel = new ViewModelProvider(this, factory).get(FavoriteViewModel.class);

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Setup products RecyclerView
        setupProductsRecyclerView();

        // Setup favorite button - ĐƠN GIẢN CHỈ CẦN CLICK
        binding.btnFavorite.setOnClickListener(v -> {
            // Gọi API thêm vào yêu thích
            favoriteViewModel.addFavorite(restaurantId);
        });

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

        // Observe add favorite action
        favoriteViewModel.addFavoriteEvent.observe(this, event -> {
            if (event == null) return;

            var resource = event.getContentIfNotHandled();
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    // Disable button trong lúc loading
                    binding.btnFavorite.setEnabled(false);
                    break;

                case SUCCESS:
                    binding.btnFavorite.setEnabled(true);
                    showToast("Đã thêm vào yêu thích");
                    break;

                case ERROR:
                    binding.btnFavorite.setEnabled(true);
                    showToast("Lỗi: " + resource.getMessage());
                    break;
            }
        });
    }

    private void setupProductsRecyclerView() {
        productAdapter = new ProductAdapter();
        productAdapter.setOnItemClickListener((product, position) -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra(Constants.KEY_PRODUCT_ID, product.getId());
            startActivity(intent);
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
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + restaurant.getPhone()));
                startActivity(intent);
            });
        } else {
            binding.layoutPhone.setVisibility(View.GONE);
        }

        // Open/Close status
        if (restaurant.isOpen()) {
            binding.tvStatus.setText("Đang mở cửa");
            binding.tvStatus.setTextColor(getColor(R.color.success));
            binding.tvOpenTime.setText(String.format("%s - %s",
                    restaurant.getOpenTime(), restaurant.getCloseTime()));
        } else {
            binding.tvStatus.setText("Đã đóng cửa");
            binding.tvStatus.setTextColor(getColor(R.color.error));
            binding.tvOpenTime.setText("Mở cửa lúc " + restaurant.getOpenTime());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
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