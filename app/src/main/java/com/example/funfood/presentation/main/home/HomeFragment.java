package com.example.funfood.presentation.main.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funfood.databinding.FragmentHomeBinding;
import com.example.funfood.presentation.base.BaseFragment;
import com.example.funfood.presentation.main.home.adapter.CategoryAdapter;
import com.example.funfood.presentation.main.home.adapter.PromotionAdapter;
import com.example.funfood.presentation.main.home.adapter.RestaurantAdapter;
import com.example.funfood.presentation.restaurant.detail.RestaurantDetailActivity;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private HomeViewModel viewModel;
    private CategoryAdapter categoryAdapter;
    private PromotionAdapter promotionAdapter;
    private RestaurantAdapter restaurantAdapter;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected FragmentHomeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupCategoryRecyclerView();
        setupPromotionRecyclerView();
        setupRestaurantRecyclerView();
        setupSwipeRefresh();
        setupSearch();

        // Load initial data
        viewModel.loadCategories();
        viewModel.loadPromotions();
        viewModel.loadRestaurants(currentPage);
    }

    @Override
    protected void observeData() {
        // Categories
        viewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case SUCCESS:
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        categoryAdapter.setItems(resource.getData());
                        binding.layoutCategories.setVisibility(View.VISIBLE);
                    }
                    break;
                case ERROR:
                    showToast("Không thể tải danh mục: " + resource.getMessage());
                    break;
            }
        });

        // Promotions
        viewModel.getPromotionsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case SUCCESS:
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        promotionAdapter.setItems(resource.getData());
                        binding.layoutPromotions.setVisibility(View.VISIBLE);
                    }
                    break;
                case ERROR:
                    showToast("Không thể tải khuyến mãi: " + resource.getMessage());
                    break;
            }
        });

        // Restaurants
        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            isLoading = false;
            binding.swipeRefresh.setRefreshing(false);

            switch (resource.getStatus()) {
                case LOADING:
                    if (currentPage == 1) {
                        showLoading();
                    }
                    isLoading = true;
                    break;

                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        if (currentPage == 1) {
                            restaurantAdapter.setItems(resource.getData());
                        } else {
                            restaurantAdapter.addItems(resource.getData());
                        }

                        // Check if last page
                        isLastPage = resource.getData().size() < 10; // Assuming limit = 10

                        // Show/hide empty state
                        if (restaurantAdapter.getItemCount() == 0) {
                            binding.tvEmptyRestaurants.setVisibility(View.VISIBLE);
                            binding.rvRestaurants.setVisibility(View.GONE);
                        } else {
                            binding.tvEmptyRestaurants.setVisibility(View.GONE);
                            binding.rvRestaurants.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                case ERROR:
                    hideLoading();
                    if (currentPage == 1) {
                        binding.tvEmptyRestaurants.setText(resource.getMessage());
                        binding.tvEmptyRestaurants.setVisibility(View.VISIBLE);
                        binding.rvRestaurants.setVisibility(View.GONE);
                    } else {
                        showToast("Không thể tải thêm nhà hàng");
                    }
                    break;
            }
        });
    }

    private void setupCategoryRecyclerView() {
        categoryAdapter = new CategoryAdapter();
        categoryAdapter.setOnItemClickListener((category, position) -> {
            // TODO: Navigate to category restaurants
            showToast("Đang phát triển: " + category.getName());
        });

        binding.rvCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvCategories.setAdapter(categoryAdapter);
    }

    private void setupPromotionRecyclerView() {
        promotionAdapter = new PromotionAdapter();
        promotionAdapter.setOnItemClickListener((promotion, position) -> {
            // TODO: Show promotion details
            showToast("Mã: " + promotion.getCode());
        });

        binding.rvPromotions.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvPromotions.setAdapter(promotionAdapter);
    }

    private void setupRestaurantRecyclerView() {
        restaurantAdapter = new RestaurantAdapter();
        restaurantAdapter.setOnItemClickListener((restaurant, position) -> {
            Intent intent = new Intent(getContext(), RestaurantDetailActivity.class);
            intent.putExtra("RESTAURANT_ID", restaurant.getId());
            startActivity(intent);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvRestaurants.setLayoutManager(layoutManager);
        binding.rvRestaurants.setAdapter(restaurantAdapter);

        // Pagination scroll listener
        binding.rvRestaurants.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadMoreRestaurants();
                    }
                }
            }
        });
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(() -> {
            currentPage = 1;
            isLastPage = false;
            viewModel.loadRestaurants(currentPage);
        });
    }

    private void setupSearch() {
        binding.etSearch.setOnClickListener(v -> {
            // TODO: Navigate to search activity
            showToast("Chức năng tìm kiếm đang phát triển");
        });
    }

    private void loadMoreRestaurants() {
        currentPage++;
        viewModel.loadRestaurants(currentPage);
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