package com.example.funfood.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast; // FIX 1: Thêm import để dùng cho thông báo (tùy chọn)

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.funfood.R;
import com.example.funfood.data.preferences.UserPreferences;
import com.example.funfood.databinding.ActivityMainBinding;
import com.example.funfood.presentation.auth.LoginActivity;
import com.example.funfood.presentation.cart.CartActivity; // FIX 2: Thêm import cho CartActivity
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userPreferences = UserPreferences.getInstance(this);

        setupNavigation();
        setupToolbar();
    }

    private void setupNavigation() {
        // Setup Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Setup BottomNavigationView
            BottomNavigationView bottomNav = binding.bottomNavigation;
            NavigationUI.setupWithNavController(bottomNav, navController);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        // FIX 3: Thêm khối else if để xử lý sự kiện click vào giỏ hàng
        else if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        // FIX 4: Thêm xử lý cho các icon còn lại (nếu có)
        else if (id == R.id.action_search) {
            Toast.makeText(this, "Chức năng tìm kiếm chưa được cài đặt", Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình tìm kiếm
            return true;
        }
        else if (id == R.id.action_notifications) {
            Toast.makeText(this, "Chức năng thông báo chưa được cài đặt", Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình thông báo
            return true;
        }

        // FIX 5: Thêm dòng này để xử lý click vào các item trên bottom nav (nếu bạn có)
        if (NavigationUI.onNavDestinationSelected(item, navController)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Clear user data
        userPreferences.logout();

        // Navigate to Login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}