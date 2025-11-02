package com.example.funfood.presentation.main.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funfood.databinding.ItemPromotionBinding; // Tạo file layout item_promotion.xml
import com.example.funfood.domain.model.Promotion;
import com.example.funfood.util.ImageUtil;

public class PromotionAdapter extends ListAdapter<Promotion, PromotionAdapter.PromotionViewHolder> {

    public PromotionAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPromotionBinding binding = ItemPromotionBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PromotionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class PromotionViewHolder extends RecyclerView.ViewHolder {
        private final ItemPromotionBinding binding;

        public PromotionViewHolder(ItemPromotionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Promotion promotion) {
            ImageUtil.loadImage(
                    binding.ivPromotionImage.getContext(),
                    promotion.getImageUrl(),
                    binding.ivPromotionImage
            );
        }
    }

    private static final DiffUtil.ItemCallback<Promotion> DIFF_CALLBACK = new DiffUtil.ItemCallback<Promotion>() {
        @Override
        public boolean areItemsTheSame(@NonNull Promotion oldItem, @NonNull Promotion newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Promotion oldItem, @NonNull Promotion newItem) {
            return oldItem.equals(newItem);
        }
    };
}