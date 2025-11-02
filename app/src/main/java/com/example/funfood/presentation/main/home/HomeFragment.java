package com.example.funfood.presentation.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.funfood.R; // Thêm import R
import com.example.funfood.databinding.FragmentHomeBinding;
import com.example.funfood.domain.model.Category;
import com.example.funfood.domain.model.Promotion; // Thêm import Promotion
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.presentation.base.BaseFragment;
import com.example.funfood.presentation.main.home.adapter.CategoryAdapter;
import com.example.funfood.presentation.main.home.adapter.PromotionAdapter;
import com.example.funfood.presentation.main.home.adapter.RestaurantAdapter;
import com.example.funfood.presentation.product.ProductSearchActivity;
import com.example.funfood.presentation.restaurant.detail.RestaurantDetailActivity;
import com.example.funfood.presentation.restaurant.list.RestaurantListActivity;
import com.example.funfood.util.AppConfig;
import com.example.funfood.util.Constants; // Sửa AppConfig thành Constants
import com.example.funfood.util.Resource;

public class HomeFragment extends BaseFragment<FragmentHomeBinding>
        implements CategoryAdapter.OnCategoryClickListener, RestaurantAdapter.OnRestaurantClickListener {

    private HomeViewModel viewModel;
    private CategoryAdapter categoryAdapter;
    private RestaurantAdapter restaurantAdapter;
    private PromotionAdapter promotionAdapter;

    @Override
    protected FragmentHomeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setupViews();
        observeData();
    }

    @Override
    protected void setupViews() {
        // Sử dụng R.string.nav_home thay vì getString(com.example.funfood.R.string.nav_home)
        binding.tvHomeTitle.setText(R.string.nav_home);

        // Setup Category RecyclerView
        categoryAdapter = new CategoryAdapter();
        categoryAdapter.setOnCategoryClickListener(this);
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(categoryAdapter);

        // Setup Promotion ViewPager2
        promotionAdapter = new PromotionAdapter();
        binding.viewPagerPromotions.setAdapter(promotionAdapter);
        setupPromotionViewPager(); // Gọi phương thức đã sửa

        // Setup Restaurant RecyclerView
        restaurantAdapter = new RestaurantAdapter();
        restaurantAdapter.setOnRestaurantClickListener(this);
        binding.rvRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRestaurants.setNestedScrollingEnabled(false);
        binding.rvRestaurants.setAdapter(restaurantAdapter);

        // Setup Click Listeners
        binding.tvSearchBar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductSearchActivity.class);
            startActivity(intent);
        });
    }

    private void setupPromotionViewPager() {
        binding.viewPagerPromotions.setClipToPadding(false);
        binding.viewPagerPromotions.setClipChildren(false);
        binding.viewPagerPromotions.setOffscreenPageLimit(3);

        // SỬA LỖI: Áp dụng setOverScrollMode cho ViewPager2, không phải cho getChildAt(0)
        binding.viewPagerPromotions.setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        binding.viewPagerPromotions.setPageTransformer(compositePageTransformer);
    }

    // Chỉ giữ lại phương thức observeData() đúng (phiên bản sử dụng Resource)
    @Override
    protected void observeData() {
        // Observe Categories
        viewModel.categories.observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;
            // TODO: Thêm xử lý Loading/Error (hiện/ẩn ProgressBar)
            // binding.progressBarCategories.setVisibility(resource.isLoading() ? View.VISIBLE : View.GONE);

            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                categoryAdapter.submitList(resource.getData());
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                showToast("Lỗi tải danh mục: " + resource.getMessage());
            }
        });

        // Observe Restaurants
        viewModel.restaurants.observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;
            // TODO: Thêm xử lý Loading/Error
            // binding.progressBarRestaurants.setVisibility(resource.isLoading() ? View.VISIBLE : View.GONE);

            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                restaurantAdapter.submitList(resource.getData());
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                showToast("Lỗi tải nhà hàng: " + resource.getMessage());
            }
        });

        // Observe Promotions
        viewModel.promotions.observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;
            // TODO: Thêm xử lý Loading/Error
            // binding.progressBarPromotions.setVisibility(resource.isLoading() ? View.VISIBLE : View.GONE);

            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                promotionAdapter.submitList(resource.getData());
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                showToast("Lỗi tải khuyến mãi: " + resource.getMessage());
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(getActivity(), RestaurantListActivity.class);
        // Sử dụng Constants.KEY_CATEGORY_ID thay vì AppConfig
        intent.putExtra(Constants.KEY_CATEGORY_ID, category.getId());
        startActivity(intent);
    }

    @Override
    public void onRestaurantClick(Restaurant restaurant) {
        Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
        // Sử dụng Constants.KEY_RESTAURANT_ID thay vì AppConfig
        intent.putExtra(Constants.KEY_RESTAURANT_ID, restaurant.getId());
        startActivity(intent);
    }
}