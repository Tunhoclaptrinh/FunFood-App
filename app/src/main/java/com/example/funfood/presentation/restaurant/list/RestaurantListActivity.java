package com.example.funfood.presentation.restaurant.list;

import android.view.LayoutInflater;
import com.example.funfood.databinding.ActivityRestaurantListBinding; // Sẽ báo đỏ, nhưng không sao
import com.example.funfood.presentation.base.BaseActivity;

// 1. Sửa class để kế thừa từ BaseActivity
public class RestaurantListActivity extends BaseActivity<ActivityRestaurantListBinding> {

    // 2. Implement các phương thức của BaseActivity
    @Override
    protected ActivityRestaurantListBinding getViewBinding() {
        // Bạn đã có file layout 'activity_restaurant_list.xml'
        return ActivityRestaurantListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // TODO: Cài đặt Toolbar, RecyclerView...
    }

    @Override
    protected void observeData() {
        // TODO: Lắng nghe dữ liệu từ ViewModel
    }
}