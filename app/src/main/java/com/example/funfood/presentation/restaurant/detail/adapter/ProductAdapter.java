package com.example.funfood.presentation.restaurant.detail.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.funfood.R;
import com.example.funfood.databinding.ItemProductBinding;
import com.example.funfood.domain.model.Product;
import com.example.funfood.presentation.base.BaseAdapter;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.ImageUtil;

public class ProductAdapter extends BaseAdapter<Product, ItemProductBinding> {

    @NonNull
    @Override
    public BaseViewHolder<ItemProductBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    @Override
    protected void bind(ItemProductBinding binding, Product product, int position) {
        // Image
        ImageUtil.loadImage(binding.getRoot().getContext(), product.getImage(), binding.ivProduct);

        // Name
        binding.tvName.setText(product.getName());

        // Description
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            binding.tvDescription.setText(product.getDescription());
            binding.tvDescription.setVisibility(View.VISIBLE);
        } else {
            binding.tvDescription.setVisibility(View.GONE);
        }

        // Price with discount
        if (product.hasDiscount()) {
            // Show original price with strikethrough
            binding.tvOriginalPrice.setText(CurrencyUtil.formatCurrency(product.getPrice()));
            binding.tvOriginalPrice.setPaintFlags(
                    binding.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            binding.tvOriginalPrice.setVisibility(View.VISIBLE);

            // Show discounted price
            binding.tvPrice.setText(CurrencyUtil.formatCurrency(product.getDiscountedPrice()));

            // Show discount badge
            binding.tvDiscount.setText("-" + product.getDiscount() + "%");
            binding.tvDiscount.setVisibility(View.VISIBLE);
        } else {
            binding.tvOriginalPrice.setVisibility(View.GONE);
            binding.tvDiscount.setVisibility(View.GONE);
            binding.tvPrice.setText(CurrencyUtil.formatCurrency(product.getPrice()));
        }

        // Availability
        if (!product.isAvailable()) {
            binding.layoutOverlay.setVisibility(View.VISIBLE);
            binding.tvUnavailable.setVisibility(View.VISIBLE);
            binding.getRoot().setAlpha(0.6f);
        } else {
            binding.layoutOverlay.setVisibility(View.GONE);
            binding.tvUnavailable.setVisibility(View.GONE);
            binding.getRoot().setAlpha(1.0f);
        }
    }
}