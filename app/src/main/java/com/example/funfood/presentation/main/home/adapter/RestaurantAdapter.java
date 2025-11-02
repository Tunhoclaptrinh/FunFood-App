package com.example.funfood.presentation.main.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funfood.databinding.ItemRestaurantBinding;
import com.example.funfood.domain.model.Restaurant;

// import com.bumptech.glide.Glide;

public class RestaurantAdapter extends ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder> {

    public RestaurantAdapter() {
        super(new DiffUtil.ItemCallback<Restaurant>() {
            @Override
            public boolean areItemsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestaurantBinding binding = ItemRestaurantBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new RestaurantViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantBinding binding;

        public RestaurantViewHolder(ItemRestaurantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Restaurant restaurant) {
            binding.tvRestaurantName.setText(restaurant.getName());
            binding.tvRestaurantAddress.setText(restaurant.getAddress());
            binding.tvRestaurantRating.setText(String.valueOf(restaurant.getRating()));

            // Dùng Glide hoặc Picasso để tải ảnh
            // Glide.with(binding.ivRestaurantImage.getContext())
            //         .load(restaurant.getImageUrl())
            //         .placeholder(R.drawable.ic_placeholder_image)
            //         .into(binding.ivRestaurantImage);
        }
    }
}