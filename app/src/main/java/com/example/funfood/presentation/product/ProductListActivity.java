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
    private String categoryName = "";

    @Override
    protected ActivityProductListBinding getViewBinding() {
        return ActivityProductListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // Get category info from intent
        categoryId = getIntent().getIntExtra(Constants.KEY_CATEGORY_ID, -1);
        categoryName = getIntent().getStringExtra("category_name");

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            if (categoryId > 0 && categoryName != null && !categoryName.isEmpty()) {
                getSupportActionBar().setTitle(categoryName);
            } else {
                getSupportActionBar().setTitle("Tất cả sản phẩm");
            }
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Setup RecyclerView
        setupRecyclerView();

        // Setup SwipeRefresh
        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.refreshProducts(categoryId);
        });

        // Load data
        if (categoryId > 0) {
            viewModel.loadProductsByCategory(categoryId, 1);
        } else {
            viewModel.loadProducts(1);
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
                            productAdapter.setItems(resource.getData());
                        } else {
                            productAdapter.addItems(resource.getData());
                        }

                        updateEmptyState();
                    }
                    break;

                case ERROR:
                    hideLoading();
                    binding.progressLoadMore.setVisibility(View.GONE);

                    if (viewModel.getCurrentPage() == 1) {
                        binding.tvEmpty.setText(resource.getMessage());
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        binding.rvProducts.setVisibility(View.GONE);
                    } else {
                        showToast("Không thể tải thêm");
                    }
                    break;
            }
        });
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

        // Infinite scroll
        binding.rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        loadMoreProducts();
                    }
                }
            }
        });
    }

    private void loadMoreProducts() {
        if (!isLoadingMore && viewModel.canLoadMore()) {
            isLoadingMore = true;
            viewModel.loadMoreProducts();
        }
    }

    private void updateEmptyState() {
        if (productAdapter.getItemCount() == 0) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.rvProducts.setVisibility(View.GONE);
            binding.tvEmpty.setText("Chưa có sản phẩm nào");
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