package com.example.funfood.presentation.main.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.funfood.databinding.ItemCategoryBinding;
import com.example.funfood.domain.model.Category;
import com.example.funfood.presentation.base.BaseAdapter;
import com.example.funfood.util.ImageUtil;

public class CategoryAdapter extends BaseAdapter<Category, ItemCategoryBinding> {

    @NonNull
    @Override
    public BaseViewHolder<ItemCategoryBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    @Override
    protected void bind(ItemCategoryBinding binding, Category category, int position) {
        // Icon (emoji or image)
        if (category.getIcon() != null && !category.getIcon().isEmpty()) {
            binding.tvIcon.setText(category.getIcon());
        }

        // Name
        binding.tvName.setText(category.getName());

        // Image background (optional)
        if (category.getImage() != null && !category.getImage().isEmpty()) {
            ImageUtil.loadImage(binding.getRoot().getContext(),
                    category.getImage(), binding.ivBackground);
        }
    }
}