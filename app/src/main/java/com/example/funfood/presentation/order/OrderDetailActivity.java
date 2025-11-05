package com.example.funfood.presentation.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.example.funfood.R; // Thêm import R
import com.example.funfood.databinding.ActivityOrderDetailBinding;
import com.example.funfood.domain.model.Order;
import com.example.funfood.domain.model.OrderItem; // Thêm import OrderItem
import com.example.funfood.presentation.base.BaseActivity;
import com.example.funfood.presentation.main.MainActivity;
import com.example.funfood.util.Constants;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.DateUtil;
import com.example.funfood.util.Resource;

public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding> {

    private OrderDetailViewModel viewModel;
    private int orderId;
    private Order currentOrder; // Lưu lại order hiện tại

    @Override
    protected ActivityOrderDetailBinding getViewBinding() {
        return ActivityOrderDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        // Get order ID from intent
        orderId = getIntent().getIntExtra(Constants.KEY_ORDER_ID, -1);
        if (orderId == -1) {
            showToast("Đơn hàng không hợp lệ");
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class); // Lỗi đã được fix

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Chi tiết đơn hàng");
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Buttons
        binding.btnTrackOrder.setOnClickListener(v -> trackOrder());
        binding.btnBackHome.setOnClickListener(v -> navigateToHome());

        // Load order details
        viewModel.loadOrder(orderId);
    }

    @Override
    protected void observeData() {
        viewModel.getOrderLiveData().observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading();
                    break;

                case SUCCESS:
                    hideLoading();
                    if (resource.getData() != null) {
                        currentOrder = resource.getData(); // Lưu đơn hàng
                        displayOrderInfo(currentOrder);
                    }
                    break;

                case ERROR:
                    hideLoading();
                    handleError(resource.getMessage());
                    break;
            }
        });
    }

    private void displayOrderInfo(Order order) {
        // Order number
        binding.tvOrderNumber.setText("Đơn hàng #" + order.getId());

        // Status
        updateOrderStatus(order.getStatus());

        // Order date
        binding.tvOrderDate.setText("Ngày đặt: " + DateUtil.formatDate(order.getCreatedAt()));

        // Delivery address
        binding.tvDeliveryAddress.setText(order.getDeliveryAddress());

        // Order items (FIX: Bỏ comment)
        if (order.getItems() != null) {
            StringBuilder itemsText = new StringBuilder();
            for (int i = 0; i < order.getItems().size(); i++) {
                OrderItem item = order.getItems().get(i);
                if (i > 0) itemsText.append("\n");
                // Lấy tên sản phẩm và số lượng từ OrderItem
                itemsText.append(item.getProductName())
                        .append(" x ")
                        .append(item.getQuantity());
            }
            binding.tvOrderItems.setText(itemsText.toString());
        }

        // Summary
        binding.tvSubtotal.setText(CurrencyUtil.formatCurrency(order.getSubtotal()));
        binding.tvDeliveryFee.setText(CurrencyUtil.formatCurrency(order.getDeliveryFee()));
        // Tính toán giảm giá (nếu API không trả về field 'discount')
        double discount = order.getSubtotal() + order.getDeliveryFee() - order.getTotal();
        binding.tvDiscount.setText(CurrencyUtil.formatCurrency(-discount));

        binding.tvTotal.setText(CurrencyUtil.formatCurrency(order.getTotal()));

        // Payment method
        String paymentMethod = getPaymentMethodName(order.getPaymentMethod());
        binding.tvPaymentMethod.setText(paymentMethod);

        // Show success layout
        binding.successContainer.setVisibility(View.VISIBLE);
    }

    private void updateOrderStatus(String status) {
        if (status == null) status = ""; // Chống crash nếu status là null
        String statusText;
        int statusColor;

        switch (status) {
            case Constants.ORDER_STATUS_PENDING:
                statusText = "Chờ xác nhận";
                statusColor = getColor(R.color.status_pending);
                break;
            case Constants.ORDER_STATUS_CONFIRMED:
                statusText = "Đã xác nhận";
                statusColor = getColor(R.color.status_confirmed);
                break;
            case Constants.ORDER_STATUS_PREPARING:
                statusText = "Đang chuẩn bị";
                statusColor = getColor(R.color.status_preparing);
                break;
            case Constants.ORDER_STATUS_DELIVERING:
                statusText = "Đang giao";
                statusColor = getColor(R.color.status_delivering);
                break;
            case Constants.ORDER_STATUS_DELIVERED:
                statusText = "Đã giao";
                statusColor = getColor(R.color.status_delivered);
                break;
            case Constants.ORDER_STATUS_CANCELLED:
                statusText = "Đã hủy";
                statusColor = getColor(R.color.status_cancelled);
                break;
            default:
                statusText = "Không xác định";
                statusColor = getColor(R.color.text_secondary);
        }

        binding.tvOrderStatus.setText(statusText);
        binding.tvOrderStatus.setTextColor(statusColor);
    }

    private String getPaymentMethodName(String method) {
        if (method == null) method = ""; // Chống crash
        switch (method) {
            case Constants.PAYMENT_CASH:
                return "Tiền mặt";
            case Constants.PAYMENT_CARD:
                return "Thẻ tín dụng";
            case Constants.PAYMENT_MOMO:
                return "Momo";
            case Constants.PAYMENT_ZALOPAY:
                return "ZaloPay";
            default:
                return "Không xác định";
        }
    }

    private void trackOrder() {
        // Navigate to order tracking screen
        Intent intent = new Intent(this, OrderTrackingActivity.class);
        intent.putExtra(Constants.KEY_ORDER_ID, orderId);
        startActivity(intent);
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.scrollView.setVisibility(View.GONE);
    }

    @Override
    protected void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.VISIBLE);
    }
}