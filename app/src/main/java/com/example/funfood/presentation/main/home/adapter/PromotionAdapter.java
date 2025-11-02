package com.example.funfood.presentation.main.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.funfood.databinding.ItemPromotionBinding;
import com.example.funfood.domain.model.Promotion;
import com.example.funfood.presentation.base.BaseAdapter;
import com.example.funfood.util.CurrencyUtil;

public class PromotionAdapter extends BaseAdapter<Promotion, ItemPromotionBinding> {

    @NonNull
    @Override
    public BaseViewHolder<ItemPromotionBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPromotionBinding binding = ItemPromotionBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BaseViewHolder<>(binding);
    }

    @Override
    protected void bind(ItemPromotionBinding binding, Promotion promotion, int position) {
        // Code
        binding.tvCode.setText(promotion.getCode());

        // Description
        binding.tvDescription.setText(promotion.getDescription());

        // Discount value
        String discountText;
        if ("percentage".equals(promotion.getDiscountType())) {
            discountText = "Giảm " + promotion.getDiscountValue() + "%";
        } else if ("fixed".equals(promotion.getDiscountType())) {
            discountText = "Giảm " + CurrencyUtil.formatCurrency(promotion.getDiscountValue());
        } else if ("delivery".equals(promotion.getDiscountType())) {
            discountText = "Miễn phí ship";
        } else {
            discountText = "Giảm giá";
        }
        binding.tvDiscount.setText(discountText);

        // Min order value
        if (promotion.getMinOrderValue() > 0) {
            binding.tvMinOrder.setText("Đơn tối thiểu: " +
                    CurrencyUtil.formatCurrency(promotion.getMinOrderValue()));
            binding.tvMinOrder.setVisibility(android.view.View.VISIBLE);
        } else {
            binding.tvMinOrder.setVisibility(android.view.View.GONE);
        }
    }
}