package com.example.funfood.presentation.main.favorite;

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

import com.example.funfood.databinding.FragmentFavoriteBinding;
import com.example.funfood.domain.model.Favorite;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.presentation.base.BaseFragment;
import com.example.funfood.presentation.main.favorite.adapter.FavoriteAdapter;
import com.example.funfood.presentation.restaurant.detail.RestaurantDetailActivity;
import com.example.funfood.util.Constants;
import com.example.funfood.util.Resource;

import java.util.List;

// --- Imports cần thiết để khởi tạo ViewModel thủ công ---
import androidx.lifecycle.ViewModel;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.FavoriteApi;
import com.example.funfood.data.repository.FavoriteRepository;
// --- Kết thúc Imports ---


public class FavoriteFragment extends BaseFragment<FragmentFavoriteBinding> {

    private FavoriteViewModel viewModel;
    private FavoriteAdapter favoriteAdapter;

    // --- FACTORY ĐỂ KHỞI TẠO VIEWMODEL ---
    /**
     * Factory tùy chỉnh để khởi tạo FavoriteViewModel bằng tay.
     * Cần thiết vì bạn không dùng Hilt cho Fragment này.
     */
    private static class FavoriteViewModelFactory implements ViewModelProvider.Factory {
        private final FavoriteRepository repository;

        public FavoriteViewModelFactory(FavoriteRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(FavoriteViewModel.class)) {
                // Bỏ qua cảnh báo unchecked cast vì chúng ta biết chắc kiểu
                //noinspection unchecked
                return (T) new FavoriteViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
    // --- KẾT THÚC FACTORY ---

    @Override
    protected FragmentFavoriteBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFavoriteBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // --- ĐÂY LÀ CHỖ SỬA LỖI ---
        // BẮT BUỘC phải khởi tạo viewModel TRƯỚC khi gọi super.onViewCreated()
        // vì super.onViewCreated() sẽ gọi observeData() và setupViews()

        // 1. Lấy RetrofitClient singleton
        RetrofitClient retrofitClient = RetrofitClient.getInstance(requireContext().getApplicationContext());
        // 2. Tạo FavoriteApi
        FavoriteApi favoriteApi = retrofitClient.createService(FavoriteApi.class);
        // 3. Tạo FavoriteRepository
        FavoriteRepository favoriteRepository = new FavoriteRepository(favoriteApi);
        // 4. Tạo ViewModelFactory
        FavoriteViewModelFactory factory = new FavoriteViewModelFactory(favoriteRepository);
        // 5. Khởi tạo ViewModel qua Factory
        viewModel = new ViewModelProvider(this, factory).get(FavoriteViewModel.class);

        // --- HẾT CHỖ SỬA LỖI ---

        // Bây giờ mới gọi super, lúc này 'viewModel' đã được khởi tạo và không còn null
        super.onViewCreated(view, savedInstanceState);

        // Các dòng này trong file gốc của bạn bị lặp lại,
        // vì BaseFragment đã gọi chúng rồi.
        // setupViews();
        // observeData();
    }

    @Override
    protected void setupViews() {
        // Khởi tạo Adapter
        favoriteAdapter = new FavoriteAdapter();
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(favoriteAdapter);

        // Xử lý sự kiện click
        favoriteAdapter.setOnFavoriteClickListener(new FavoriteAdapter.OnFavoriteClickListener() {

            @Override
            public void onItemClick(Restaurant restaurant) {
                // Chuyển sang màn hình chi tiết nhà hàng
                Intent intent = new Intent(requireContext(), RestaurantDetailActivity.class);
                intent.putExtra(Constants.KEY_RESTAURANT_ID, restaurant.getId());
                startActivity(intent);
            }

            @Override
            public void onRemoveClick(Favorite favorite) {
                // Gọi ViewModel để xóa (an toàn vì viewModel đã được khởi tạo)
                viewModel.removeFavorite(favorite);
            }
        });
    }

    @Override
    protected void observeData() {
        // 1. Observe danh sách yêu thích
        // Dòng này sẽ KHÔNG BỊ CRASH nữa vì 'viewModel' không còn null
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
        // Đảm bảo viewModel đã được khởi tạo trước khi gọi
        if (viewModel != null) {
            viewModel.fetchFavorites();
        }
    }
}