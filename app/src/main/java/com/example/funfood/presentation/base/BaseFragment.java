package com.example.funfood.presentation.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.funfood.util.NetworkUtil;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {

    protected VB binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = getViewBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        observeData();
    }

    /**
     * Khởi tạo ViewBinding
     */
    protected abstract VB getViewBinding(LayoutInflater inflater, ViewGroup container);

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
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showToastLong(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Show snackbar
     */
    protected void showSnackbar(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    protected void showSnackbarLong(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    protected void showSnackbarWithAction(String message, String actionText, View.OnClickListener action) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                    .setAction(actionText, action)
                    .show();
        }
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
        return getContext() != null && NetworkUtil.isNetworkAvailable(getContext());
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}