package com.example.funfood.presentation.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.databinding.ActivityEditProfileBinding;
import com.example.funfood.domain.model.User;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.main.profile.ProfileViewModel;
import com.example.funfood.util.ImageUtil;
import com.example.funfood.util.Resource;

public class EditProfileActivity extends BaseActivity<ActivityEditProfileBinding> {

    private ProfileViewModel viewModel;
    private User currentUser;

    @Override
    protected ActivityEditProfileBinding getViewBinding() {
        return ActivityEditProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Chỉnh sửa thông tin");
        }

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Load current user data
        loadUserData();

        // Save button click
        binding.btnSave.setOnClickListener(v -> saveProfile());

        // Avatar click (TODO: implement image picker)
        binding.ivAvatar.setOnClickListener(v -> {
            showToast("Chức năng đổi ảnh đại diện đang phát triển");
        });
    }

    @Override
    protected void observeData() {
        // Observe user data
        viewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                currentUser = user;
                displayUserData(user);
            }
        });

        // Observe update result
        viewModel.getUpdateProfileResult().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;

                case SUCCESS:
                    hideLoading();
                    showToast("Cập nhật thông tin thành công!");

                    // Return result to previous screen
                    setResult(RESULT_OK);
                    finish();
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

    private void loadUserData() {
        // User data will be loaded automatically via ViewModel
    }

    private void displayUserData(User user) {
        binding.etName.setText(user.getName());
        binding.etEmail.setText(user.getEmail());
        binding.etPhone.setText(user.getPhone());
        binding.etAddress.setText(user.getAddress());

        // Email is read-only
        binding.etEmail.setEnabled(false);

        // Load avatar
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            ImageUtil.loadCircularImage(this, user.getAvatar(), binding.ivAvatar);
        }
    }

    private void saveProfile() {
        String name = binding.etName.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();

        // Validate
        if (name.isEmpty()) {
            binding.tilName.setError("Tên không được để trống");
            binding.etName.requestFocus();
            return;
        }
        binding.tilName.setError(null);

        if (!phone.isEmpty() && !isValidPhone(phone)) {
            binding.tilPhone.setError("Số điện thoại không hợp lệ");
            binding.etPhone.requestFocus();
            return;
        }
        binding.tilPhone.setError(null);

        // Check network
        if (!isNetworkAvailable()) {
            showNetworkError();
            return;
        }

        // Call ViewModel to update
        viewModel.updateProfile(name, phone, address, currentUser != null ? currentUser.getAvatar() : null);
    }

    private boolean isValidPhone(String phone) {
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        return cleanPhone.matches("^0[0-9]{9,10}$");
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnSave.setEnabled(false);
        binding.scrollView.setAlpha(0.5f);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnSave.setEnabled(true);
        binding.scrollView.setAlpha(1.0f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clearUpdateProfileResult();
    }
}