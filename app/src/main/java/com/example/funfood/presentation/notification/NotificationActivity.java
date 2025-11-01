package com.example.funfood.presentation.notification;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.funfood.R;
import com.example.funfood.databinding.ActivityNotificationBinding;
import com.example.funfood.domain.model.Notification;
import com.example.funfood.presentation.notification.adapter.NotificationAdapter;
import com.example.funfood.util.Resource;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnNotificationClickListener {

    private ActivityNotificationBinding binding;
    private NotificationViewModel viewModel;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo ViewModel qua Hilt
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        setupToolbar();
        setupRecyclerView();
        setupSwipeRefresh();
        observeViewModel();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbarNotifications);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter(this);
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotifications.setAdapter(adapter);
        // Thêm logic pagination nếu cần
    }

    private void setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.fetchNotifications(true);
        });
    }

    private void observeViewModel() {
        viewModel.notifications.observe(this, resource -> {
            if (resource == null) return;

            // Xử lý trạng thái Loading
            binding.swipeRefreshLayout.setRefreshing(resource.status == Resource.Status.LOADING && resource.data != null);
            binding.progressBar.setVisibility(resource.status == Resource.Status.LOADING && resource.data == null ? View.VISIBLE : View.GONE);

            // Xử lý trạng thái Success
            if (resource.status == Resource.Status.SUCCESS) {
                if (resource.data != null && !resource.data.isEmpty()) {
                    adapter.submitList(resource.data);
                    binding.rvNotifications.setVisibility(View.VISIBLE);
                    binding.tvEmptyNotifications.setVisibility(View.GONE);
                } else {
                    binding.rvNotifications.setVisibility(View.GONE);
                    binding.tvEmptyNotifications.setVisibility(View.VISIBLE);
                }
            }

            // Xử lý trạng thái Error
            if (resource.status == Resource.Status.ERROR && resource.data == null) {
                binding.tvEmptyNotifications.setText(resource.message);
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
        getMenuInflater().inflate(R.menu.menu_notification, menu); // Tạo file menu_notification.xml
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

    // --- Implement Adapter Listeners ---

    @Override
    public void onItemClick(Notification notification) {
        viewModel.markAsRead(notification);
        // TODO: Điều hướng đến chi tiết Order hoặc Promotion
        // Ví dụ:
        // if ("order".equals(notification.getType())) {
        //     Intent intent = new Intent(this, OrderDetailActivity.class);
        //     intent.putExtra("ORDER_ID", notification.getRefId());
        //     startActivity(intent);
        // }
    }

    @Override
    public void onDeleteClick(Notification notification) {
        viewModel.deleteNotification(notification);
    }
}