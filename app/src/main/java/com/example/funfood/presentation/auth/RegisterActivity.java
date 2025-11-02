package com.example.funfood.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.databinding.ActivityRegisterBinding;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.main.MainActivity;
import com.example.funfood.util.Resource;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    private RegisterViewModel viewModel;

    @Override
    protected ActivityRegisterBinding getViewBinding() {
        return ActivityRegisterBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Back button
        if (findViewById(com.example.funfood.R.id.btnBack) != null) {
            findViewById(com.example.funfood.R.id.btnBack).setOnClickListener(v -> finish());
        }

        // Register button
        binding.btnRegister.setOnClickListener(v -> handleRegister());

        // Login link
        if (binding.tvLogin != null) {
            binding.tvLogin.setOnClickListener(v -> finish());
        }
    }

    @Override
    protected void observeData() {
        viewModel.getRegisterResult().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;

                case SUCCESS:
                    hideLoading();
                    showToast("Đăng ký thành công!");
                    navigateToMain();
                    break;

                case ERROR:
                    hideLoading();
                    handleError(resource.getMessage());
                    break;
            }
        });
    }

    private void handleRegister() {
        String name = binding.etName.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();

        // Validate
        if (name.isEmpty()) {
            binding.tilName.setError("Họ tên không được để trống");
            return;
        }
        binding.tilName.setError(null);

        if (phone.isEmpty()) {
            binding.tilPhone.setError("Số điện thoại không được để trống");
            return;
        }
        binding.tilPhone.setError(null);

        if (email.isEmpty()) {
            binding.tilEmail.setError("Email không được để trống");
            return;
        }
        binding.tilEmail.setError(null);

        if (password.isEmpty()) {
            binding.tilPassword.setError("Mật khẩu không được để trống");
            return;
        }
        if (password.length() < 6) {
            binding.tilPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }
        binding.tilPassword.setError(null);

        // Check network
        if (!isNetworkAvailable()) {
            showNetworkError();
            return;
        }

        // Call ViewModel
        viewModel.register(email, password, name, phone, address);
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRegister.setEnabled(false);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnRegister.setEnabled(true);
    }
}