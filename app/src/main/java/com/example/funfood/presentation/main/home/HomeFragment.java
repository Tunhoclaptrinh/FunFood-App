package com.example.funfood.presentation.main.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.funfood.databinding.FragmentHomeBinding;
import com.example.funfood.presentation.base.BaseFragment;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    @Override
    protected FragmentHomeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        // TODO: Setup views
        binding.tvHomeTitle.setText("Trang chủ");
    }

    @Override
    protected void observeData() {
        // TODO: Observe ViewModel
    }
}