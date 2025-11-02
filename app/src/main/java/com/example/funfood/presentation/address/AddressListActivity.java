package com.example.funfood.presentation.address;

import android.content.Intent;
import android.os.Bundle;

import com.example.funfood.databinding.ActivityAddressListBinding;
import com.example.funfood.presentation.base.BaseActivity;

// 1. Kế thừa từ BaseActivity và chỉ định ViewBinding
public class AddressListActivity extends BaseActivity<ActivityAddressListBinding> {

    // 2. Cung cấp phương thức getViewBinding
    @Override
    protected ActivityAddressListBinding getViewBinding() {
        return ActivityAddressListBinding.inflate(getLayoutInflater());
    }

    // 3. Cài đặt các view
    @Override
    protected void setupViews() {
        // Cài đặt Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Gán sự kiện click cho nút quay lại trên Toolbar
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Gán sự kiện click cho nút FAB (Thêm địa chỉ)
        binding.fabAddAddress.setOnClickListener(v -> {
            // TODO: Mở màn hình AddAddressActivity
            // Intent intent = new Intent(this, AddAddressActivity.class);
            // startActivity(intent);
            showToast("Chức năng thêm địa chỉ đang phát triển!");
        });
    }

    // 4. Cài đặt observeData (để trống nếu chưa dùng ViewModel)
    @Override
    protected void observeData() {
        // TODO: Lắng nghe dữ liệu từ AddressViewModel
    }
}