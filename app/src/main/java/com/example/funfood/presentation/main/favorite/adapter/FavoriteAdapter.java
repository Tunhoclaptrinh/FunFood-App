package com.example.funfood.presentation.main.favorite.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funfood.R;
import com.example.funfood.databinding.ItemFavoriteRestaurantBinding;
import com.example.funfood.domain.model.Favorite;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.util.ImageUtil; // Giả sử bạn đã có lớp này

public class FavoriteAdapter extends ListAdapter<Favorite, FavoriteAdapter.FavoriteViewHolder> {

    public interface OnFavoriteClickListener {
        void onItemClick(Restaurant restaurant);
        void onRemoveClick(Favorite favorite);
    }

    private OnFavoriteClickListener listener;

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.listener = listener;
    }

    public FavoriteAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteRestaurantBinding binding = ItemFavoriteRestaurantBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private final ItemFavoriteRestaurantBinding binding;
        private final OnFavoriteClickListener listener;

        public FavoriteViewHolder(ItemFavoriteRestaurantBinding binding, OnFavoriteClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        void bind(Favorite favorite) {
            if (favorite == null || favorite.getRestaurant() == null) {
                return;
            }

            Restaurant restaurant = favorite.getRestaurant();
            binding.tvRestaurantName.setText(restaurant.getName());
            binding.tvRestaurantRating.setText(String.valueOf(restaurant.getRating()));

            ImageUtil.loadImage(
                    binding.ivRestaurantImage.getContext(),
                    restaurant.getImage(),
                    binding.ivRestaurantImage,
                    R.drawable.ic_placeholder_image
            );

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(restaurant);
                }
            });

            binding.btnRemoveFavorite.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveClick(favorite);
                }
            });
        }
    }

    private static final DiffUtil.ItemCallback<Favorite> DIFF_CALLBACK = new DiffUtil.ItemCallback<Favorite>() {
        @Override
        public boolean areItemsTheSame(@NonNull Favorite oldItem, @NonNull Favorite newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Favorite oldItem, @NonNull Favorite newItem) {
            return oldItem.getRestaurant().getId() == newItem.getRestaurant().getId();
        }
    };
}