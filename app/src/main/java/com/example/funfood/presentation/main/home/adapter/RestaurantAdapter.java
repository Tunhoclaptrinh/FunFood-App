package com.example.funfood.presentation.main.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.funfood.databinding.ItemRestaurantBinding;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.presentation.base.BaseAdapter;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.ImageUtil;

public class RestaurantAdapter extends BaseAdapter<Restaurant, ItemRestaurantBinding> {

    @NonNull
    @Override
    public BaseViewHolder<ItemRestaurantBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    @Override
    protected void bind(ItemRestaurantBinding binding, Restaurant restaurant, int position) {
        // Image
        ImageUtil.loadImage(binding.getRoot().getContext(), restaurant.getImage(), binding.ivRestaurant);

        // Name
        binding.tvName.setText(restaurant.getName());

        // Rating
        binding.tvRating.setText(String.format("%.1f", restaurant.getRating()));

        // Delivery info
        binding.tvDeliveryTime.setText(restaurant.getDeliveryTime());
        binding.tvDeliveryFee.setText(CurrencyUtil.formatCurrency(restaurant.getDeliveryFee()));

        // Open/Close status
        if (restaurant.isOpen()) {
            binding.tvStatus.setText("Đang mở cửa");
            binding.tvStatus.setTextColor(binding.getRoot().getContext().getColor(
                    com.example.funfood.R.color.success));
        } else {
            binding.tvStatus.setText("Đã đóng cửa");
            binding.tvStatus.setTextColor(binding.getRoot().getContext().getColor(
                    com.example.funfood.R.color.error));
        }

        // Description
        if (restaurant.getDescription() != null && !restaurant.getDescription().isEmpty()) {
            binding.tvDescription.setText(restaurant.getDescription());
            binding.tvDescription.setVisibility(android.view.View.VISIBLE);
        } else {
            binding.tvDescription.setVisibility(android.view.View.GONE);
        }
    }
}