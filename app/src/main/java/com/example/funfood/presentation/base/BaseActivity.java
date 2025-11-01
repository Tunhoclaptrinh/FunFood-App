package com.example.funfood.presentation.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.example.funfood.util.NetworkUtil;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {

    protected VB binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        setContentView(binding.getRoot());

        setupViews();
        observeData();
    }

    /**
     * Khởi tạo ViewBinding
     */
    protected abstract VB getViewBinding();

    /**
     * Setup views, listeners, etc.
     */
    protected abstract void setupViews();

    /**
     * Observe LiveData/ViewModel
     */
    protected abstract void observeData();

    /**
     * Show toast message
     */
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToastLong(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Show snackbar
     */
    protected void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackbarLong(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    protected void showSnackbarWithAction(String message, String actionText, View.OnClickListener action) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction(actionText, action)
                .show();
    }

    /**
     * Show/Hide loading
     */
    protected void showLoading() {
        // Override in child class if needed
    }

    protected void hideLoading() {
        // Override in child class if needed
    }

    /**
     * Check network connection
     */
    protected boolean isNetworkAvailable() {
        return NetworkUtil.isNetworkAvailable(this);
    }

    protected void showNetworkError() {
        showSnackbar("No internet connection");
    }

    /**
     * Handle error
     */
    protected void handleError(String message) {
        if (message != null && !message.isEmpty()) {
            showSnackbar(message);
        } else {
            showSnackbar("An error occurred");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}