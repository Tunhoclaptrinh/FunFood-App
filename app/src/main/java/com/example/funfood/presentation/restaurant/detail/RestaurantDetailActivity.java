package com.example.funfood.presentation.restaurant.detail;

import android.view.LayoutInflater;
import com.example.funfood.databinding.ActivityRestaurantDetailBinding; // Sẽ báo đỏ, nhưng không sao
import com.example.funfood.presentation.base.BaseActivity;

// 1. Sửa class để kế thừa từ BaseActivity
public class RestaurantDetailActivity extends BaseActivity<ActivityRestaurantDetailBinding> {

    // 2. Implement các phương thức của BaseActivity
    @Override
    protected ActivityRestaurantDetailBinding getViewBinding() {
        // Bạn đã có file layout 'activity_restaurant_detail.xml'
        return ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // TODO: Cài đặt Toolbar, hiển thị thông tin nhà hàng...
    }

    @Override
    protected void observeData() {
        // TODO: Lắng nghe dữ liệu từ ViewModel
    }
}