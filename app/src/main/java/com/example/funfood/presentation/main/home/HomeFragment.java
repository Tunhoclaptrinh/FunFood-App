package com.example.funfood.presentation.main.home;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funfood.databinding.FragmentHomeBinding;
import com.example.funfood.domain.model.Category;
import com.example.funfood.presentation.base.BaseFragment;
import com.example.funfood.presentation.main.home.adapter.CategoryAdapter;
import com.example.funfood.presentation.main.home.adapter.ProductAdapter;
import com.example.funfood.presentation.main.home.adapter.PromotionAdapter;
import com.example.funfood.presentation.main.home.adapter.RestaurantAdapter;
import com.example.funfood.presentation.product.ProductDetailActivity;
import com.example.funfood.presentation.restaurant.detail.RestaurantDetailActivity;
import com.example.funfood.util.Resource;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private HomeViewModel viewModel;
    private CategoryAdapter categoryAdapter;
    private PromotionAdapter promotionAdapter;
    private RestaurantAdapter restaurantAdapter;
    private ProductAdapter productAdapter;

    private boolean isLoadingMore = false;
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

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
        setupProductRecyclerView();
        setupSwipeRefresh();
        setupSearch();

        // Load initial data
        loadInitialData();
    }

    @Override
    protected void observeData() {
        observeCategories();
        observePromotions();
        observeRestaurants();
        observeProducts();
        observeFilters();
    }

    private void loadInitialData() {
        viewModel.loadCategories();
        viewModel.loadPromotions();
        viewModel.loadFeaturedProducts(); // Load featured products
        viewModel.loadRestaurants(1);
    }

    // Setup RecyclerViews
    private void setupCategoryRecyclerView() {
        categoryAdapter = new CategoryAdapter();
        categoryAdapter.setOnItemClickListener((category, position) -> {
            handleCategoryClick(category);
        });

        binding.rvCategories.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvCategories.setAdapter(categoryAdapter);
    }

    private void setupPromotionRecyclerView() {
        promotionAdapter = new PromotionAdapter();
        promotionAdapter.setOnItemClickListener((promotion, position) -> {
            showToast("Mã: " + promotion.getCode() + " - " + promotion.getDescription());
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

        // Infinite scroll
        binding.rvRestaurants.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoadingMore && viewModel.canLoadMore()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 10) {
                        loadMoreRestaurants();
                    }
                }
            }
        });
    }

    private void setupProductRecyclerView() {
        productAdapter = new ProductAdapter();
        productAdapter.setOnItemClickListener((product, position) -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            startActivity(intent);
        });

        binding.rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvProducts.setAdapter(productAdapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(() -> {
            refreshData();
        });
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel previous search
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Debounce search (wait 500ms after user stops typing)
                searchRunnable = () -> {
                    String query = s.toString().trim();
                    if (!query.isEmpty()) {
                        viewModel.searchRestaurants(query);
                        // Hide categories, promotions and featured products when searching
                        binding.layoutCategories.setVisibility(View.GONE);
                        binding.layoutPromotions.setVisibility(View.GONE);
                        binding.layoutProducts.setVisibility(View.GONE);
                        binding.tvProductTitle.setText("Sản phẩm");
                    } else {
                        viewModel.refreshRestaurants();
                        viewModel.loadFeaturedProducts(); // Reload featured products
                        // Show categories, promotions and products
                        binding.layoutCategories.setVisibility(View.VISIBLE);
                        binding.layoutPromotions.setVisibility(View.VISIBLE);
                        binding.layoutProducts.setVisibility(View.VISIBLE);
                        binding.tvProductTitle.setText("Sản phẩm nổi bật");
                    }
                };
                searchHandler.postDelayed(searchRunnable, 500);
            }
        });

        // Clear search when click X
        binding.ivClearSearch.setOnClickListener(v -> {
            binding.etSearch.setText("");
            viewModel.resetFilters();
        });
    }

    // Observe data
    private void observeCategories() {
        viewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case SUCCESS:
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        categoryAdapter.setItems(resource.getData());
                        binding.layoutCategories.setVisibility(View.VISIBLE);
                    } else {
                        binding.layoutCategories.setVisibility(View.GONE);
                    }
                    break;

                case ERROR:
                    binding.layoutCategories.setVisibility(View.GONE);
                    break;
            }
        });
    }

    private void observePromotions() {
        viewModel.getPromotionsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case SUCCESS:
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        promotionAdapter.setItems(resource.getData());
                        binding.layoutPromotions.setVisibility(View.VISIBLE);
                    } else {
                        binding.layoutPromotions.setVisibility(View.GONE);
                    }
                    break;

                case ERROR:
                    binding.layoutPromotions.setVisibility(View.GONE);
                    break;
            }
        });
    }

    private void observeRestaurants() {
        viewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            isLoadingMore = false;
            binding.swipeRefresh.setRefreshing(false);

            switch (resource.getStatus()) {
                case LOADING:
                    if (viewModel.getCurrentPage() == 1) {
                        showLoading();
                    } else {
                        isLoadingMore = true;
                    }
                    break;

                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        if (viewModel.getCurrentPage() == 1) {
                            restaurantAdapter.setItems(resource.getData());
                        } else {
                            restaurantAdapter.addItems(resource.getData());
                        }

                        updateRestaurantVisibility();
                    }
                    break;

                case ERROR:
                    hideLoading();
                    if (viewModel.getCurrentPage() == 1) {
                        binding.tvEmptyRestaurants.setText(resource.getMessage());
                        binding.tvEmptyRestaurants.setVisibility(View.VISIBLE);
                        binding.rvRestaurants.setVisibility(View.GONE);
                    } else {
                        showToast("Không thể tải thêm");
                    }
                    break;
            }
        });
    }

    private void observeProducts() {
        viewModel.getProductsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    binding.progressProducts.setVisibility(View.VISIBLE);
                    break;

                case SUCCESS:
                    binding.progressProducts.setVisibility(View.GONE);
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
                        productAdapter.setItems(resource.getData());
                        binding.layoutProducts.setVisibility(View.VISIBLE);
                        binding.tvEmptyProducts.setVisibility(View.GONE);

                        // Only hide restaurants when filtering by category
                        Integer selectedCategoryId = viewModel.getSelectedCategoryId().getValue();
                        if (selectedCategoryId != null && selectedCategoryId > 0) {
                            binding.rvRestaurants.setVisibility(View.GONE);
                            binding.tvRestaurantTitle.setVisibility(View.GONE);
                            binding.tvProductTitle.setText("Sản phẩm");
                            binding.tvViewAllProducts.setVisibility(View.GONE);
                        } else {
                            // Show both products and restaurants on home
                            binding.rvRestaurants.setVisibility(View.VISIBLE);
                            binding.tvRestaurantTitle.setVisibility(View.VISIBLE);
                            binding.tvProductTitle.setText("Sản phẩm nổi bật");

                            // Show "View all" if there are many products
                            if (resource.getData().size() >= 10) {
                                binding.tvViewAllProducts.setVisibility(View.VISIBLE);
                            } else {
                                binding.tvViewAllProducts.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        binding.layoutProducts.setVisibility(View.VISIBLE);
                        binding.tvEmptyProducts.setVisibility(View.VISIBLE);
                    }
                    break;

                case ERROR:
                    binding.progressProducts.setVisibility(View.GONE);
                    binding.layoutProducts.setVisibility(View.GONE);
                    break;
            }
        });

        // Handle "View all" click
        binding.tvViewAllProducts.setOnClickListener(v -> {
            showToast("Chức năng xem tất cả sản phẩm đang phát triển");
            // TODO: Navigate to products list screen
        });
    }

    private void observeFilters() {
        viewModel.getSelectedCategoryId().observe(getViewLifecycleOwner(), categoryId -> {
            updateCategorySelection(categoryId);

            if (categoryId != null) {
                // Show back button to clear filter
                binding.btnClearFilter.setVisibility(View.VISIBLE);
            } else {
                binding.btnClearFilter.setVisibility(View.GONE);
            }
        });

        binding.btnClearFilter.setOnClickListener(v -> {
            viewModel.clearCategoryFilter();
            viewModel.loadFeaturedProducts(); // Reload featured products
            binding.layoutProducts.setVisibility(View.VISIBLE);
            binding.rvRestaurants.setVisibility(View.VISIBLE);
            binding.tvRestaurantTitle.setVisibility(View.VISIBLE);
            binding.tvProductTitle.setText("Sản phẩm nổi bật");
        });
    }

    // Handlers
    private void handleCategoryClick(Category category) {
        viewModel.filterByCategory(category.getId());
        showToast("Đang tải sản phẩm " + category.getName());
    }

    private void loadMoreRestaurants() {
        if (!isLoadingMore && viewModel.canLoadMore()) {
            isLoadingMore = true;
            viewModel.loadMoreRestaurants();
        }
    }

    private void refreshData() {
        viewModel.refreshRestaurants();
        viewModel.loadCategories();
        viewModel.loadPromotions();
        viewModel.loadFeaturedProducts(); // Reload featured products

        // Reset UI
        binding.layoutProducts.setVisibility(View.VISIBLE); // Keep products visible
        binding.rvRestaurants.setVisibility(View.VISIBLE);
        binding.tvRestaurantTitle.setVisibility(View.VISIBLE);
        binding.btnClearFilter.setVisibility(View.GONE);
        binding.tvProductTitle.setText("Sản phẩm nổi bật");
    }

    private void updateRestaurantVisibility() {
        if (restaurantAdapter.getItemCount() == 0) {
            binding.tvEmptyRestaurants.setVisibility(View.VISIBLE);
            binding.rvRestaurants.setVisibility(View.GONE);
        } else {
            binding.tvEmptyRestaurants.setVisibility(View.GONE);
            binding.rvRestaurants.setVisibility(View.VISIBLE);
        }
    }

    private void updateCategorySelection(Integer selectedId) {
        // Update adapter to highlight selected category
        if (categoryAdapter != null) {
            categoryAdapter.setSelectedCategoryId(selectedId != null ? selectedId : -1);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchHandler != null && searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }
}