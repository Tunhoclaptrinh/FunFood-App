package com.example.funfood.presentation.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.funfood.databinding.FragmentHomeBinding;
import com.example.funfood.presentation.base.BaseFragment;
import com.example.funfood.presentation.main.home.adapter.CategoryAdapter;
import com.example.funfood.presentation.main.home.adapter.RestaurantAdapter;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private HomeViewModel viewModel;
    private CategoryAdapter categoryAdapter;
    private RestaurantAdapter restaurantAdapter;

    @Override
    protected FragmentHomeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ViewModel
        // (Nếu dùng Hilt/Koin thì inject, ở đây dùng cách cơ bản)
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupViews();
        observeData();
    }

    @Override
    protected void setupViews() {
        binding.tvHomeTitle.setText("Trang chủ"); // Đã có

        // Setup Category RecyclerView
        categoryAdapter = new CategoryAdapter();
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategories.setAdapter(categoryAdapter);

        // Setup Restaurant RecyclerView
        restaurantAdapter = new RestaurantAdapter();
        binding.rvRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRestaurants.setAdapter(restaurantAdapter);

        // Setup Click Listeners
        binding.tvSearchBar.setOnClickListener(v -> {
            // TODO: Chuyển sang màn hình tìm kiếm
            Toast.makeText(getContext(), "Chuyển sang màn hình tìm kiếm", Toast.LENGTH_SHORT).show();
            // Ví dụ: startActivity(new Intent(getActivity(), ProductSearchActivity.class));
        });
    }

    @Override
    protected void observeData() {
        // Observe Categories
        viewModel.categories.observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                categoryAdapter.submitList(categories);
            }
        });

        // Observe Restaurants
        viewModel.restaurants.observe(getViewLifecycleOwner(), restaurants -> {
            if (restaurants != null) {
                restaurantAdapter.submitList(restaurants);
            }
        });

        // TODO: Observe Promotions (để set cho ViewPager2)
        viewModel.promotions.observe(getViewLifecycleOwner(), promotions -> {
            // Cập nhật ViewPager/Banner
        });
    }
}