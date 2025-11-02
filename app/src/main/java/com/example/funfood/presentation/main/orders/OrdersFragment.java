package com.example.funfood.presentation.main.orders;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.funfood.databinding.FragmentOrdersBinding;
import com.example.funfood.presentation.base.BaseFragment;

public class OrdersFragment extends BaseFragment<FragmentOrdersBinding> {

    @Override
    protected FragmentOrdersBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentOrdersBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        binding.tvOrdersTitle.setText("Đơn hàng");
    }

    @Override
    protected void observeData() {
        // TODO: Observe ViewModel
    }
}