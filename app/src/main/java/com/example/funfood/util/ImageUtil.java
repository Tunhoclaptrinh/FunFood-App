package com.example.funfood.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.funfood.R;

public class ImageUtil {

    private static final RequestOptions DEFAULT_OPTIONS = new RequestOptions()
            .placeholder(R.drawable.ic_placeholder_image)
            .error(R.drawable.ic_error_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    /**
     * Load image from URL
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_placeholder_image);
            return;
        }

        Glide.with(context)
                .load(url)
                .apply(DEFAULT_OPTIONS)
                .into(imageView);
    }

    /**
     * Load image with custom placeholder
     */
    public static void loadImage(Context context, String url, ImageView imageView,
                                 @DrawableRes int placeholder) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(placeholder);
            return;
        }

        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * Load circular image (for avatar)
     */
    public static void loadCircularImage(Context context, String url, ImageView imageView) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_avatar_placeholder);
            return;
        }

        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * Load image with custom size
     */
    public static void loadImage(Context context, String url, ImageView imageView,
                                 int width, int height) {
        if (url == null || url.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_placeholder_image);
            return;
        }

        RequestOptions options = new RequestOptions()
                .override(width, height)
                .placeholder(R.drawable.ic_placeholder_image)
                .error(R.drawable.ic_error_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    /**
     * Clear Glide cache
     */
    public static void clearCache(Context context) {
        Glide.get(context).clearMemory();
        new Thread(() -> Glide.get(context).clearDiskCache()).start();
    }

    /**
     * Preload image
     */
    public static void preloadImage(Context context, String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .load(url)
                    .preload();
        }
    }
}