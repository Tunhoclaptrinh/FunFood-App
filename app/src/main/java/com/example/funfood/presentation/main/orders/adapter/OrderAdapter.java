package com.example.funfood.presentation.main.orders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.funfood.R;
import com.example.funfood.databinding.ItemOrderProductBinding;
import com.example.funfood.domain.model.OrderItem;
import com.example.funfood.presentation.base.BaseAdapter;
import com.example.funfood.util.ImageUtil;

import java.text.DecimalFormat;

public class OrderAdapter extends BaseAdapter<OrderItem, ItemOrderProductBinding> {

    private final Context context;
    private final DecimalFormat formatter = new DecimalFormat("#,### đ");

    public OrderAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public BaseViewHolder<ItemOrderProductBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderProductBinding binding = ItemOrderProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    @Override
    protected void bind(ItemOrderProductBinding binding, OrderItem item, int position) {
        binding.tvProductName.setText(item.getProductName());
        binding.tvQuantity.setText("x" + item.getQuantity());
        binding.tvProductPrice.setText(formatter.format(item.getPrice()));

        double itemTotal = item.getQuantity() * item.getPrice() - item.getDiscount();
        binding.tvItemTotal.setText(formatter.format(itemTotal));

        if (item.getDiscount() > 0) {
            binding.tvProductDiscount.setVisibility(ViewGroup.VISIBLE);
            binding.tvProductDiscount.setText("Giảm " + formatter.format(item.getDiscount()));
        } else {
            binding.tvProductDiscount.setVisibility(ViewGroup.GONE);
        }

        // Load ảnh sản phẩm (nếu có URL — ở đây demo ảnh mặc định)
        Glide.with(context)
                .load(R.drawable.ic_placeholder_image)
                .centerCrop()
                .into(binding.imgProduct);
    }
}