package com.example.funfood.presentation.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.funfood.R;
import com.example.funfood.databinding.ActivityNotificationBinding;
import com.example.funfood.domain.model.Notification;
import com.example.funfood.presentation.notification.adapter.NotificationAdapter;
import com.example.funfood.presentation.order.OrderDetailActivity;
import com.example.funfood.util.Constants;
import com.example.funfood.util.Resource;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnNotificationClickListener {

    private ActivityNotificationBinding binding;
    private NotificationViewModel viewModel;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupSwipeRefresh();
        observeViewModel();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbarNotifications);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter(this);
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotifications.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.fetchNotifications(true);
        });
    }

    private void observeViewModel() {
        viewModel.notifications.observe(this, resource -> {
            if (resource == null) return;

            binding.swipeRefreshLayout.setRefreshing(
                    resource.getStatus() == Resource.Status.LOADING && resource.getData() != null
            );

            binding.progressBar.setVisibility(
                    resource.getStatus() == Resource.Status.LOADING && resource.getData() == null ?
                            View.VISIBLE : View.GONE
            );

            if (resource.getStatus() == Resource.Status.SUCCESS) {
                if (resource.getData() != null && !resource.getData().isEmpty()) {
                    adapter.submitList(resource.getData());
                    binding.rvNotifications.setVisibility(View.VISIBLE);
                    binding.tvEmptyNotifications.setVisibility(View.GONE);
                } else {
                    binding.rvNotifications.setVisibility(View.GONE);
                    binding.tvEmptyNotifications.setVisibility(View.VISIBLE);
                }
            }

            if (resource.getStatus() == Resource.Status.ERROR && resource.getData() == null) {
                binding.tvEmptyNotifications.setText(resource.getMessage());
                binding.tvEmptyNotifications.setVisibility(View.VISIBLE);
                binding.rvNotifications.setVisibility(View.GONE);
            }
        });

        viewModel.toastEvent.observe(this, event -> {
            String message = event.getContentIfNotHandled();
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_mark_all_read) {
            viewModel.markAllAsRead();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Notification notification) {
        viewModel.markAsRead(notification);
        // Điều hướng dựa trên loại thông báo
        String type = notification.getType();
        if (type == null) return; // Đảm bảo an toàn

        switch (type) {
            case "order":
                // Điều hướng đến Chi tiết đơn hàng
                Intent orderIntent = new Intent(this, OrderDetailActivity.class);
                orderIntent.putExtra(Constants.KEY_ORDER_ID, notification.getRefId());
                startActivity(orderIntent);
                break;

            case "promotion":
                // TODO: Điều hướng đến màn hình Khuyến mãi chi tiết (nếu có)
                // Ví dụ:
                // Intent promoIntent = new Intent(this, PromotionDetailActivity.class);
                // promoIntent.putExtra(Constants.KEY_PROMOTION_ID, notification.getRefId());
                // startActivity(promoIntent);
                Toast.makeText(this, "Mở chi tiết khuyến mãi #" + notification.getRefId(), Toast.LENGTH_SHORT).show();
                break;

            case "system":
            case "favorite":
            default:
                // Không cần điều hướng, hoặc hiển thị thông báo chung
                Toast.makeText(this, "Thông báo hệ thống.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDeleteClick(Notification notification) {
        viewModel.deleteNotification(notification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}