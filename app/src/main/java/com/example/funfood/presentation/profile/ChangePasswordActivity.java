package com.example.funfood.presentation.profile;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.databinding.ActivityChangePasswordBinding;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.main.profile.ProfileViewModel;
import com.example.funfood.util.Resource;

public class ChangePasswordActivity extends BaseActivity<ActivityChangePasswordBinding> {

    private ProfileViewModel viewModel;

    @Override
    protected ActivityChangePasswordBinding getViewBinding() {
        return ActivityChangePasswordBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Đổi mật khẩu");
        }

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Change password button click
        binding.btnChangePassword.setOnClickListener(v -> changePassword());
    }

    @Override
    protected void observeData() {
        // Observe change password result
        viewModel.getChangePasswordResult().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;

                case SUCCESS:
                    hideLoading();
                    showToast("Đổi mật khẩu thành công!");

                    // Clear input fields
                    binding.etCurrentPassword.setText("");
                    binding.etNewPassword.setText("");
                    binding.etConfirmPassword.setText("");

                    // Return to previous screen after delay
                    binding.getRoot().postDelayed(this::finish, 1500);
                    break;

                case ERROR:
                    hideLoading();
                    handleError(resource.getMessage());
                    break;
            }
        });

        // Observe error messages
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                showToast(error);
                viewModel.clearMessages();
            }
        });
    }

    private void changePassword() {
        String currentPassword = binding.etCurrentPassword.getText().toString().trim();
        String newPassword = binding.etNewPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        // Validate
        if (currentPassword.isEmpty()) {
            binding.tilCurrentPassword.setError("Mật khẩu hiện tại không được để trống");
            binding.etCurrentPassword.requestFocus();
            return;
        }
        binding.tilCurrentPassword.setError(null);

        if (newPassword.isEmpty()) {
            binding.tilNewPassword.setError("Mật khẩu mới không được để trống");
            binding.etNewPassword.requestFocus();
            return;
        }
        if (newPassword.length() < 6) {
            binding.tilNewPassword.setError("Mật khẩu mới phải có ít nhất 6 ký tự");
            binding.etNewPassword.requestFocus();
            return;
        }
        binding.tilNewPassword.setError(null);

        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.setError("Xác nhận mật khẩu không được để trống");
            binding.etConfirmPassword.requestFocus();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            binding.tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            binding.etConfirmPassword.requestFocus();
            return;
        }
        binding.tilConfirmPassword.setError(null);

        if (currentPassword.equals(newPassword)) {
            binding.tilNewPassword.setError("Mật khẩu mới phải khác mật khẩu cũ");
            binding.etNewPassword.requestFocus();
            return;
        }

        // Check network
        if (!isNetworkAvailable()) {
            showNetworkError();
            return;
        }

        // Call ViewModel to change password
        viewModel.changePassword(currentPassword, newPassword);
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnChangePassword.setEnabled(false);
        binding.scrollView.setAlpha(0.5f);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnChangePassword.setEnabled(true);
        binding.scrollView.setAlpha(1.0f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clearChangePasswordResult();
    }
}