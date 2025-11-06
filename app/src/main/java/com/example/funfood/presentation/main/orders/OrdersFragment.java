package com.example.funfood.presentation.main.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.funfood.databinding.FragmentOrdersBinding;
import com.example.funfood.domain.model.Order;
import com.example.funfood.domain.model.OrderItem;
import com.example.funfood.presentation.base.BaseFragment;

import com.example.funfood.presentation.main.orders.adapter.OrderAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends BaseFragment<FragmentOrdersBinding> {

    private OrdersViewModel viewModel;
    private OrderAdapter adapter;
    private String selectedStatus = "all";
    private int currentPage = 1;

    @Override
    protected FragmentOrdersBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentOrdersBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(OrdersViewModel.class);

        setupToolbar();
        setupOrderRecyclerView();
        setupSwipeRefresh();

        setupFilterChips();
        // Load initial data
        viewModel.loadOrders(1, selectedStatus, currentPage);
    }

    @Override
    protected void observeData() {
        // TODO: Observe ViewModel
        observeOrders();
        observeLoadingState();

    }

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
    }


    private void observeLoadingState() {
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (binding != null) {
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (!isLoading) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private void setupOrderRecyclerView(){
        adapter = new OrderAdapter(requireContext());
        binding.recyclerViewOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewOrders.setAdapter(adapter);
    };
    private void setupSwipeRefresh(){
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            adapter.clear();
            viewModel.loadOrders(1,selectedStatus, currentPage);
        });
    };

    private void setupFilterChips() {
        ChipGroup chipGroup = binding.chipGroupStatus;

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip == null) {
                // Nếu không có chip nào được chọn, có thể coi là "Tất cả"
                selectedStatus = "all";
            } else {
                // Sử dụng if-else if thay cho switch
                if (checkedId == com.example.funfood.R.id.chipPending) {
                    selectedStatus = "pending";
                } else if (checkedId == com.example.funfood.R.id.chipConfirmed) {
                    selectedStatus = "confirmed";
                } else if (checkedId == com.example.funfood.R.id.chipPreparing) {
                    selectedStatus = "preparing";
                } else if (checkedId == com.example.funfood.R.id.chipDelivering) {
                    selectedStatus = "delivering";
                } else if (checkedId == com.example.funfood.R.id.chipDelivered) {
                    selectedStatus = "delivered";
                } else if (checkedId == com.example.funfood.R.id.chipCancelled) {
                    selectedStatus = "cancelled";
                } else { // Bao gồm cả R.id.chipAll
                    selectedStatus = "all";
                }
            }

            currentPage = 1;
            adapter.clear();
            viewModel.loadOrders(1,selectedStatus, currentPage);

        });
    }
    private void observeOrders(){
        viewModel.getOrdersLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response == null) return;

            if (response.isSuccess() && response.getData() != null) {
                List<Order> orders = response.getData();

                if (orders.isEmpty()) {
                    showEmptyState(true);
                } else {
                    showEmptyState(false);
                    adapter.clear();

                    // Nếu Order có danh sách OrderItem
                    List<com.example.funfood.domain.model.OrderItem> allItems = new ArrayList<>();
                    for (Order order : orders) {
                        if (order.getItems() != null) {
                            allItems.addAll(order.getItems());
                        }
                    }
                    adapter.setItems(allItems);
                }
            } else {
                showEmptyState(true);
                Toast.makeText(requireContext(), "Không thể tải đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    };

    private void showEmptyState(boolean show) {
        binding.layoutEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.recyclerViewOrders.setVisibility(show ? View.GONE : View.VISIBLE);
    }






}