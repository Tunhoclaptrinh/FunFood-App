package com.example.funfood.presentation.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funfood.databinding.ActivityProductListBinding;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.main.home.adapter.ProductAdapter;
import com.example.funfood.util.Constants;

public class ProductListActivity extends BaseActivity<ActivityProductListBinding> {

    private ProductListViewModel viewModel;
    private ProductAdapter productAdapter;
    private boolean isLoadingMore = false;
    private int categoryId = -1;
    private int restaurantId = -1;
    private String title = "";

    @Override
    protected ActivityProductListBinding getViewBinding() {
        return ActivityProductListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // Get filter info from intent (optional)
        categoryId = getIntent().getIntExtra(Constants.KEY_CATEGORY_ID, -1);
        restaurantId = getIntent().getIntExtra(Constants.KEY_RESTAURANT_ID, -1);
        title = getIntent().getStringExtra("title");

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

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

            // Set title
            if (title != null && !title.isEmpty()) {
                getSupportActionBar().setTitle(title);
            } else if (categoryId > 0) {
                getSupportActionBar().setTitle("Sản phẩm theo danh mục");
            } else if (restaurantId > 0) {
                getSupportActionBar().setTitle("Thực đơn");
            } else {
                getSupportActionBar().setTitle("Tất cả sản phẩm");
            }
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter();
        productAdapter.setOnItemClickListener((product, position) -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra(Constants.KEY_PRODUCT_ID, product.getId());
            startActivity(intent);
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvProducts.setLayoutManager(layoutManager);
        binding.rvProducts.setAdapter(productAdapter);

        // Setup infinite scroll
        binding.rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3
                            && firstVisibleItemPosition >= 0) {
                        loadMoreProducts();
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
        if (restaurantId > 0) {
            // Load products by restaurant
            viewModel.loadProductsByRestaurant(restaurantId, 1);
        } else if (categoryId > 0) {
            // Load products by category
            viewModel.loadProductsByCategory(categoryId, 1);
        } else {
            // Load all products
            viewModel.loadProducts(1);
        }
    }

    private void refreshData() {
        if (restaurantId > 0) {
            viewModel.refreshProductsByRestaurant(restaurantId);
        } else if (categoryId > 0) {
            viewModel.refreshProductsByCategory(categoryId);
        } else {
            viewModel.refreshProducts(-1);
        }
    }

    private void loadMoreProducts() {
        if (!isLoadingMore && viewModel.canLoadMore()) {
            isLoadingMore = true;
            viewModel.loadMoreProducts();
        }
    }

    @Override
    protected void observeData() {
        // Observe products
        viewModel.getProductsLiveData().observe(this, resource -> {
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
                            productAdapter.setItems(resource.getData());
                        } else {
                            // Next pages - append
                            productAdapter.addItems(resource.getData());
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
                                resource.getMessage() : "Không thể tải danh sách sản phẩm");
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        binding.rvProducts.setVisibility(View.GONE);
                    } else {
                        // Error on next pages
                        showToast("Không thể tải thêm");
                    }
                    break;
            }
        });
    }

    private void updateEmptyState() {
        if (productAdapter.getItemCount() == 0) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.rvProducts.setVisibility(View.GONE);

            if (restaurantId > 0) {
                binding.tvEmpty.setText("Nhà hàng chưa có sản phẩm nào");
            } else if (categoryId > 0) {
                binding.tvEmpty.setText("Chưa có sản phẩm nào trong danh mục này");
            } else {
                binding.tvEmpty.setText("Chưa có sản phẩm nào");
            }
        } else {
            binding.tvEmpty.setVisibility(View.GONE);
            binding.rvProducts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvProducts.setVisibility(View.GONE);
        binding.tvEmpty.setVisibility(View.GONE);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.rvProducts.setVisibility(View.VISIBLE);
    }
}