package com.example.funfood.presentation.restaurant.detail.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.funfood.R;
import com.example.funfood.databinding.ItemProductBinding;
import com.example.funfood.domain.model.Product;
import com.example.funfood.domain.model.Restaurant;
import com.example.funfood.presentation.base.BaseAdapter;
import com.example.funfood.util.CurrencyUtil;
import com.example.funfood.util.ImageUtil;

public class ProductAdapter extends BaseAdapter<Product, ItemProductBinding> {

    // 1. Định nghĩa interface cho các sự kiện click
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnRestaurantClickListener {
        void onRestaurantClick(Restaurant restaurant);
    }

    // 2. Khai báo listeners
    private OnProductClickListener productClickListener;
    private OnRestaurantClickListener restaurantClickListener;

    // 3. Tạo setters cho listeners
    public void setOnProductClickListener(OnProductClickListener listener) {
        this.productClickListener = listener;
    }

    // (Listener cho nhà hàng có thể không cần thiết trong màn hình chi tiết nhà hàng,
    // nhưng chúng ta thêm vào để đồng bộ)
    public void setOnRestaurantClickListener(OnRestaurantClickListener listener) {
        this.restaurantClickListener = listener;
    }

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

        // 4. Binding thông tin nhà hàng
        Restaurant restaurant = product.getRestaurant();
        if (restaurant != null) {
            binding.layoutRestaurant.setVisibility(View.VISIBLE);
            binding.tvRestaurantName.setText(restaurant.getName());

            // Load ảnh logo nhà hàng
            ImageUtil.loadImage(
                    binding.getRoot().getContext(),
                    restaurant.getImage(),
                    binding.ivRestaurantLogo
            );

            // 5. Set Click Listener cho nhà hàng
            // (Trong màn hình chi tiết nhà hàng, có thể bạn không muốn nhấn vào
            // nhà hàng nữa, nhưng chúng ta vẫn thêm logic)
            binding.layoutRestaurant.setOnClickListener(v -> {
                if (restaurantClickListener != null) {
                    restaurantClickListener.onRestaurantClick(restaurant);
                }
            });

        } else {
            binding.layoutRestaurant.setVisibility(View.GONE);
        }

        // 6. Set Click Listener cho toàn bộ item sản phẩm
        binding.getRoot().setOnClickListener(v -> {
            if (productClickListener != null && product.isAvailable()) {
                productClickListener.onProductClick(product);
            }
        });
    }
}