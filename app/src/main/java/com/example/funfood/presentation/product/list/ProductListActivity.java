package com.example.funfood.presentation.product.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.funfood.R;
import com.example.funfood.databinding.ActivityProductListBinding;
import com.example.funfood.domain.model.Product;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.product.ProductDetailActivity;
import com.example.funfood.presentation.restaurant.detail.adapter.ProductAdapter; // Tái sử dụng adapter
import com.example.funfood.util.Constants;
import com.example.funfood.util.Resource;
import java.util.List;

public class ProductListActivity extends BaseActivity<ActivityProductListBinding> {

    private ProductListViewModel viewModel;
    private ProductAdapter productAdapter;

    @Override
    protected ActivityProductListBinding getViewBinding() {
        return ActivityProductListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

        setupToolbar();
        setupRecyclerView();

        // Tải trang đầu tiên
        viewModel.loadProducts();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Tất cả sản phẩm");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rvProducts.setLayoutManager(gridLayoutManager);
        binding.rvProducts.setAdapter(productAdapter);

        // Bấm vào sản phẩm
        productAdapter.setOnItemClickListener((product, position) -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra(Constants.KEY_PRODUCT_ID, product.getId());
            startActivity(intent);
        });

        // Logic phân trang (cuộn vô tận)
        binding.rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = gridLayoutManager.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                // Kiểm tra xem đã cuộn gần đến cuối chưa
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 2 && firstVisibleItemPosition >= 0) {
                    viewModel.loadProducts(); // Tải thêm
                }
            }
        });
    }

    @Override
    protected void observeData() {
        viewModel.getProductsLiveData().observe(this, resource -> {
            if (resource == null) return;

            // Kiểm tra xem đây có phải là phân trang hay không (list đã có dữ liệu)
            boolean isLoadMore = resource.getData() != null && !resource.getData().isEmpty();

            switch (resource.getStatus()) {
                case LOADING:
                    if (isLoadMore) {
                        binding.progressBarLoadMore.setVisibility(View.VISIBLE);
                    } else {
                        binding.progressBar.setVisibility(View.VISIBLE);
                    }
                    break;
                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null && !resource.getData().isEmpty()) {
//                        productAdapter.submitList(resource.getData());
                    }
                    break;
                case ERROR:
                    hideLoading();
                    showToast("Lỗi: " + resource.getMessage());
                    break;
            }
        });
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvProducts.setVisibility(View.GONE);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.progressBarLoadMore.setVisibility(View.GONE);
        binding.rvProducts.setVisibility(View.VISIBLE);
    }
}