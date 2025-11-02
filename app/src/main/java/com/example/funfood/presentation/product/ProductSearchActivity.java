package com.example.funfood.presentation.product;

import android.view.LayoutInflater;
import com.example.funfood.databinding.ActivityProductSearchBinding;
import com.example.funfood.presentation.base.BaseActivity;

// 1. Sửa class để kế thừa từ BaseActivity
public class ProductSearchActivity extends BaseActivity<ActivityProductSearchBinding> {

    // 2. Implement các phương thức của BaseActivity
    @Override
    protected ActivityProductSearchBinding getViewBinding() {
        // Bạn sẽ cần tạo file layout tên là 'activity_product_search.xml'
        return ActivityProductSearchBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // TODO: Cài đặt Toolbar, RecyclerView, Listener... cho màn hình tìm kiếm
    }

    @Override
    protected void observeData() {
        // TODO: Lắng nghe dữ liệu từ ViewModel
    }
}