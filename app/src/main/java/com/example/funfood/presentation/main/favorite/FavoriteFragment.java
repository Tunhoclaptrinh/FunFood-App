package com.example.funfood.presentation.main.favorite;

import android.content.Intent; // <-- THÊM IMPORT NÀY
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.funfood.databinding.FragmentFavoriteBinding;
import com.example.funfood.domain.model.Favorite;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.presentation.base.BaseFragment;
import com.example.funfood.presentation.main.favorite.adapter.FavoriteAdapter;

// <-- THÊM 2 IMPORT NÀY -->
import com.example.funfood.presentation.restaurant.detail.RestaurantDetailActivity;
import com.example.funfood.util.Constants;

import com.example.funfood.util.Resource;

import java.util.List;


public class FavoriteFragment extends BaseFragment<FragmentFavoriteBinding> {

    private FavoriteViewModel viewModel;
    private FavoriteAdapter favoriteAdapter;

    @Override
    protected FragmentFavoriteBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFavoriteBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo ViewModel qua Hilt
        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        setupViews();
        observeData();
    }

    @Override
    protected void setupViews() {
        // Khởi tạo Adapter
        favoriteAdapter = new FavoriteAdapter();
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(favoriteAdapter);

        // Xử lý sự kiện click
        favoriteAdapter.setOnFavoriteClickListener(new FavoriteAdapter.OnFavoriteClickListener() {

            // --- ĐÂY LÀ PHẦN CẬP NHẬT ---
            @Override
            public void onItemClick(Restaurant restaurant) {
                // Chuyển sang màn hình chi tiết nhà hàng
                Intent intent = new Intent(requireContext(), RestaurantDetailActivity.class);
                // Sử dụng hằng số (Constants) mà RestaurantDetailActivity yêu cầu
                intent.putExtra(Constants.KEY_RESTAURANT_ID, restaurant.getId());
                startActivity(intent);
            }
            // --- KẾT THÚC CẬP NHẬT ---

            @Override
            public void onRemoveClick(Favorite favorite) {
                // Gọi ViewModel để xóa
                viewModel.removeFavorite(favorite);
            }
        });
    }

    @Override
    protected void observeData() {
        // 1. Observe danh sách yêu thích
        viewModel.favorites.observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.rvFavorites.setVisibility(View.GONE);
                    binding.tvEmptyFavorites.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    List<Favorite> favorites = resource.getData();
                    if (favorites != null && !favorites.isEmpty()) {
                        favoriteAdapter.submitList(favorites);
                        binding.rvFavorites.setVisibility(View.VISIBLE);
                        binding.tvEmptyFavorites.setVisibility(View.GONE);
                    } else {
                        // Danh sách rỗng
                        binding.rvFavorites.setVisibility(View.GONE);
                        binding.tvEmptyFavorites.setVisibility(View.VISIBLE);
                        binding.tvEmptyFavorites.setText("Bạn chưa có nhà hàng yêu thích nào");
                    }
                    break;
                case ERROR:
                    // Hiển thị lỗi
                    binding.progressBar.setVisibility(View.GONE);
                    binding.rvFavorites.setVisibility(View.GONE);
                    binding.tvEmptyFavorites.setVisibility(View.VISIBLE);
                    binding.tvEmptyFavorites.setText(resource.getMessage());
                    break;
            }
        });

        // 2. Observe sự kiện xóa (để hiển thị Toast)
        viewModel.removeFavoriteEvent.observe(getViewLifecycleOwner(), event -> {
            Resource<Object> resource = event.getContentIfNotHandled();
            if (resource == null) return; // Đã xử lý

            switch (resource.getStatus()) {
                case LOADING:
                    // Có thể hiển thị loading nhỏ
                    break;
                case SUCCESS:
                    Toast.makeText(requireContext(), "Đã cập nhật danh sách", Toast.LENGTH_SHORT).show();
                    // ViewModel đã tự động tải lại danh sách
                    break;
                case ERROR:
                    Toast.makeText(requireContext(), "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tải lại dữ liệu mỗi khi người dùng quay lại tab này
        viewModel.fetchFavorites();
    }
}