package com.example.funfood.presentation.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.R;
import com.example.funfood.databinding.FragmentProfileBinding;
import com.example.funfood.presentation.address.AddressListActivity;
import com.example.funfood.presentation.auth.LoginActivity;
import com.example.funfood.presentation.base.BaseFragment;
import com.example.funfood.util.ImageUtil;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    private ProfileViewModel viewModel;

    @Override
    protected FragmentProfileBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        setupClickListeners();
    }

    @Override
    protected void observeData() {
        // Observe user data
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvName.setText(user.getName());
                binding.tvEmail.setText(user.getEmail());
                binding.tvPhone.setText(user.getPhone() != null ? user.getPhone() : "Chưa cập nhật");

                // Load avatar
                if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                    ImageUtil.loadCircularImage(requireContext(), user.getAvatar(), binding.ivAvatar);
                }
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                showToast(error);
                viewModel.clearMessages();
            }
        });

        // Observe success messages
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), success -> {
            if (success != null && !success.isEmpty()) {
                showToast(success);
                viewModel.clearMessages();
            }
        });

        // Observe logout event
        viewModel.getLogoutEvent().observe(getViewLifecycleOwner(), event -> {
            Boolean shouldLogout = event.getContentIfNotHandled();
            if (shouldLogout != null && shouldLogout) {
                navigateToLogin();
            }
        });
    }

    private void setupClickListeners() {
        // Avatar click - Edit profile
        binding.ivAvatar.setOnClickListener(v -> navigateToEditProfile());
        binding.ivEditProfile.setOnClickListener(v -> navigateToEditProfile());

        // My Orders
        binding.layoutMyOrders.setOnClickListener(v -> {
            // Navigate to orders fragment
            if (getActivity() != null) {
                // Assuming you have bottom navigation
                // getActivity().findViewById(R.id.navigation_orders).performClick();
                showToast("Chuyển đến tab Đơn hàng");
            }
        });

        // Addresses
        binding.layoutAddresses.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AddressListActivity.class));
        });

        // Favorites
        binding.layoutFavorites.setOnClickListener(v -> {
            // Navigate to favorites fragment
            if (getActivity() != null) {
                showToast("Chuyển đến tab Yêu thích");
            }
        });

        // Change Password
        binding.layoutChangePassword.setOnClickListener(v -> {
            showChangePasswordDialog();
        });

        // Notifications Settings
        binding.layoutNotifications.setOnClickListener(v -> {
            showToast("Cài đặt thông báo (Coming soon)");
        });

        // Language
        binding.layoutLanguage.setOnClickListener(v -> {
            showToast("Đổi ngôn ngữ (Coming soon)");
        });

        // Help & Support
        binding.layoutHelp.setOnClickListener(v -> {
            showToast("Trợ giúp & Hỗ trợ (Coming soon)");
        });

        // About
        binding.layoutAbout.setOnClickListener(v -> {
            showAboutDialog();
        });

        // Logout
        binding.buttonLogout.setOnClickListener(v -> {
            showLogoutConfirmDialog();
        });
    }

    private void navigateToEditProfile() {
        // Navigate to EditProfileActivity
        // startActivity(new Intent(requireContext(), EditProfileActivity.class));
        showToast("Chỉnh sửa thông tin (Coming soon)");
    }

    private void showChangePasswordDialog() {
        // Show dialog to change password
        showToast("Đổi mật khẩu (Coming soon)");
    }

    private void showLogoutConfirmDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    viewModel.logout();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showAboutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Về FunFood")
                .setMessage("FunFood v1.0.0\n\n" +
                        "Ứng dụng đặt đồ ăn trực tuyến\n\n" +
                        "© 2024 FunFood Team")
                .setPositiveButton("OK", null)
                .show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    protected void showLoading() {
        if (binding != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.scrollView.setAlpha(0.5f);
        }
    }

    @Override
    protected void hideLoading() {
        if (binding != null) {
            binding.progressBar.setVisibility(View.GONE);
            binding.scrollView.setAlpha(1.0f);
        }
    }
}