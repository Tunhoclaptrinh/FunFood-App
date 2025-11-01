package com.example.funfood.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.databinding.ActivityLoginBinding;
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.main.MainActivity;
import com.example.funfood.util.Resource;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private LoginViewModel viewModel;

    @Override
    protected ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Login button click
        binding.btnLogin.setOnClickListener(v -> handleLogin());

        // Register link click
        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    @Override
    protected void observeData() {
        // Observe login result
        viewModel.getLoginResult().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;

                case SUCCESS:
                    hideLoading();
                    showToast("Đăng nhập thành công!");
                    navigateToMain();
                    break;

                case ERROR:
                    hideLoading();
                    handleError(resource.getMessage());
                    break;
            }
        });
    }

    private void handleLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        // Validate
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
        viewModel.login(email, password);
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
        binding.btnLogin.setEnabled(false);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setEnabled(true);
    }
}