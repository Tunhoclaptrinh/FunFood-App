package com.example.funfood.presentation.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.R;
import com.example.funfood.databinding.FragmentProfileBinding;
import com.example.funfood.data.preferences.UserPreferences;
import com.example.funfood.presentation.base.BaseFragment;
import com.example.funfood.presentation.profile.ChangePasswordActivity;
import com.example.funfood.presentation.main.MainActivity;


public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    private ProfileViewModel viewModel;
    private UserPreferences userPreferences;

    @Override
    protected FragmentProfileBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {

        userPreferences = UserPreferences.getInstance(requireContext());

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        loadUserProfile();

        binding.buttonLogout.setOnClickListener(v -> {
            viewModel.logout();
        });

        // Gán sự kiện click cho nút Sửa Profile
        binding.ivEditProfile.setOnClickListener(v -> {
            navigateToEditProfile();
        });

        // Gán sự kiện click cho mục Đổi mật khẩu
        binding.layoutChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Gán sự kiện click cho các mục khác
        binding.layoutNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.example.funfood.presentation.notification.NotificationActivity.class);
            startActivity(intent);
        });

        binding.layoutAddresses.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.example.funfood.presentation.address.AddressListActivity.class);
            startActivity(intent);
        });

        // CHUYỂN SANG TAB YÊU THÍCH
        binding.layoutFavorites.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                // Chuyển sang tab yêu thích (ID đúng là navigation_favorites)
                View bottomNav = mainActivity.findViewById(R.id.bottom_navigation);
                if (bottomNav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
                    ((com.google.android.material.bottomnavigation.BottomNavigationView) bottomNav)
                            .setSelectedItemId(R.id.navigation_favorites);
                }
            }
        });

        // Các mục chưa có chức năng
        binding.layoutMyOrders.setOnClickListener(v -> showToast("Chức năng 'Đơn hàng' đang được phát triển!"));
        binding.layoutLanguage.setOnClickListener(v -> showToast("Chức năng 'Ngôn ngữ' đang được phát triển!"));
        binding.layoutHelp.setOnClickListener(v -> showToast("Chức năng 'Hỗ trợ' đang được phát triển!"));
        binding.layoutAbout.setOnClickListener(v -> showToast("Chức năng 'Về FunFood' đang được phát triển!"));
    }

    // Cung cấp phương thức observeData
    @Override
    protected void observeData() {

        // 1. Lắng nghe sự kiện logout
        viewModel.getLogoutEvent().observe(getViewLifecycleOwner(), event -> {
            Boolean shouldLogout = event.getContentIfNotHandled();
            if (shouldLogout != null && shouldLogout) {

                // ViewModel đã xử lý xong (xóa data),
                // giờ Fragment thực hiện điều hướng (chuyển màn hình)

                Intent intent = new Intent(requireContext(), com.example.funfood.presentation.auth.LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // Đóng MainActivity (màn hình chính)
                requireActivity().finish();
            }
        });

        // 2. Lắng nghe thông tin người dùng (thay cho hàm loadUserProfile())
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvName.setText(user.getName());
                binding.tvEmail.setText(user.getEmail());
            }
        });

        // 3. Lắng nghe thông báo (lỗi hoặc thành công)
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                showToast(message);
                viewModel.clearMessages(); // Xóa thông báo sau khi hiển thị
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                showToast(message);
                viewModel.clearMessages(); // Xóa thông báo sau khi hiển thị
            }
        });
    }

    private void loadUserProfile() {
        // Lấy dữ liệu trực tiếp từ UserPreferences
        String name = userPreferences.getUserName();
        String email = userPreferences.getUserEmail();

        // Hiển thị lên TextView
        if (name != null) binding.tvName.setText(name);
        if (email != null) binding.tvEmail.setText(email);

        // (Bạn cũng có thể di chuyển logic này vào ViewModel nếu muốn)
    }

    private void navigateToEditProfile() {
        // Navigate to EditProfileActivity
        Intent intent = new Intent(requireContext(), com.example.funfood.presentation.profile.EditProfileActivity.class);
        startActivityForResult(intent, REQUEST_EDIT_PROFILE);
    }

    private static final int REQUEST_EDIT_PROFILE = 1001;

    // Bạn có thể cần thêm phương thức onActivityResult để cập nhật lại thông tin
    // sau khi người dùng sửa profile thành công
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == android.app.Activity.RESULT_OK) {
            // Khi quay lại từ màn hình EditProfile, tải lại thông tin
            showToast("Cập nhật thông tin thành công!");
            loadUserProfile();
        }
    }
}