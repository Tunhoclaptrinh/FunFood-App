package com.example.funfood.presentation.cart.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.funfood.databinding.ItemCartBinding;
import com.example.funfood.domain.model.CartItem;
import com.example.funfood.presentation.base.BaseAdapter;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.ImageUtil;

public class CartAdapter extends BaseAdapter<CartItem, ItemCartBinding> {

    public interface OnQuantityChangeListener {
        void onQuantityChanged(int cartItemId, int newQuantity);
    }

    public interface OnDeleteListener {
        void onDeleteItem(int cartItemId);
    }

    private OnQuantityChangeListener quantityListener;
    private OnDeleteListener deleteListener;

    @NonNull
    @Override
    public BaseViewHolder<ItemCartBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    @Override
    protected void bind(ItemCartBinding binding, CartItem item, int position) {
        // Product image
        ImageUtil.loadImage(binding.getRoot().getContext(), item.getProduct().getImage(),
                binding.ivProduct);

        // Product name
        binding.tvProductName.setText(item.getProduct().getName());

        // Product price
        binding.tvPrice.setText(CurrencyUtil.formatCurrency(item.getProduct().getPrice()));

        // Item total
        binding.tvItemTotal.setText(CurrencyUtil.formatCurrency(item.getItemTotal()));

        // Quantity controls
        binding.tvQuantity.setText(String.valueOf(item.getQuantity()));

        binding.btnMinus.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (quantityListener != null) {
                quantityListener.onQuantityChanged(item.getId(), newQuantity);
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            if (quantityListener != null) {
                quantityListener.onQuantityChanged(item.getId(), newQuantity);
            }
        });

        // Delete button
        binding.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteItem(item.getId());
            }
        });
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityListener = listener;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.deleteListener = listener;
    }
}