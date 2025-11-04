package com.example.funfood.presentation.restaurant.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funfood.databinding.ActivityRestaurantListBinding;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.main.home.adapter.RestaurantAdapter;
import com.example.funfood.presentation.restaurant.detail.RestaurantDetailActivity;
import com.example.funfood.util.Constants;

public class RestaurantListActivity extends BaseActivity<ActivityRestaurantListBinding> {

    private RestaurantListViewModel viewModel;
    private RestaurantAdapter restaurantAdapter;
    private boolean isLoadingMore = false;
    private int categoryId = -1;
    private String categoryName = "";

    @Override
    protected ActivityRestaurantListBinding getViewBinding() {
        return ActivityRestaurantListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // Get category info from intent (optional)
        categoryId = getIntent().getIntExtra(Constants.KEY_CATEGORY_ID, -1);
        categoryName = getIntent().getStringExtra("category_name");

        viewModel = new ViewModelProvider(this).get(RestaurantListViewModel.class);

        // Setup toolbar
        setupToolbar();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup SwipeRefresh
        setupSwipeRefresh();

        // Load initial data
        loadData();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            // Set title based on category
            if (categoryId > 0 && categoryName != null && !categoryName.isEmpty()) {
                getSupportActionBar().setTitle(categoryName);
            } else {
                getSupportActionBar().setTitle("Tất cả nhà hàng");
            }
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        restaurantAdapter = new RestaurantAdapter();
        restaurantAdapter.setOnItemClickListener((restaurant, position) -> {
            Intent intent = new Intent(this, RestaurantDetailActivity.class);
            intent.putExtra(Constants.KEY_RESTAURANT_ID, restaurant.getId());
            startActivity(intent);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvRestaurants.setLayoutManager(layoutManager);
        binding.rvRestaurants.setAdapter(restaurantAdapter);

        // Setup infinite scroll
        binding.rvRestaurants.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if scrolling down
                if (dy <= 0) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                // Load more when reaching near bottom
                if (!isLoadingMore && viewModel.canLoadMore()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2
                            && firstVisibleItemPosition >= 0) {
                        loadMoreRestaurants();
                    }
                }
            }
        });
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(() -> {
            refreshData();
        });
    }

    private void loadData() {
        if (categoryId > 0) {
            viewModel.loadRestaurantsByCategory(categoryId, 1);
        } else {
            viewModel.loadRestaurants(1);
        }
    }

    private void refreshData() {
        viewModel.refreshRestaurants(categoryId);
    }

    private void loadMoreRestaurants() {
        if (!isLoadingMore && viewModel.canLoadMore()) {
            isLoadingMore = true;
            viewModel.loadMoreRestaurants();
        }
    }

    @Override
    protected void observeData() {
        // Observe restaurants
        viewModel.getRestaurantsLiveData().observe(this, resource -> {
            if (resource == null) return;

            isLoadingMore = false;
            binding.swipeRefresh.setRefreshing(false);

            switch (resource.getStatus()) {
                case LOADING:
                    if (viewModel.getCurrentPage() == 1) {
                        showLoading();
                    } else {
                        isLoadingMore = true;
                        binding.progressLoadMore.setVisibility(View.VISIBLE);
                    }
                    break;

                case SUCCESS:
                    hideLoading();
                    binding.progressLoadMore.setVisibility(View.GONE);

                    if (resource.getData() != null) {
                        if (viewModel.getCurrentPage() == 1) {
                            // First page - replace all
                            restaurantAdapter.setItems(resource.getData());
                        } else {
                            // Next pages - append
                            restaurantAdapter.addItems(resource.getData());
                        }

                        updateEmptyState();
                    }
                    break;

                case ERROR:
                    hideLoading();
                    binding.progressLoadMore.setVisibility(View.GONE);

                    if (viewModel.getCurrentPage() == 1) {
                        // Error on first page
                        binding.tvEmpty.setText(resource.getMessage() != null ?
                                resource.getMessage() : "Không thể tải danh sách nhà hàng");
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        binding.rvRestaurants.setVisibility(View.GONE);
                    } else {
                        // Error on next pages
                        showToast("Không thể tải thêm");
                    }
                    break;
            }
        });
    }

    private void updateEmptyState() {
        if (restaurantAdapter.getItemCount() == 0) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.rvRestaurants.setVisibility(View.GONE);

            if (categoryId > 0) {
                binding.tvEmpty.setText("Chưa có nhà hàng nào trong danh mục này");
            } else {
                binding.tvEmpty.setText("Chưa có nhà hàng nào");
            }
        } else {
            binding.tvEmpty.setVisibility(View.GONE);
            binding.rvRestaurants.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvRestaurants.setVisibility(View.GONE);
        binding.tvEmpty.setVisibility(View.GONE);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.rvRestaurants.setVisibility(View.VISIBLE);
    }
}