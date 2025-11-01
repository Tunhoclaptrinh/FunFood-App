package com.example.funfood.presentation.notification.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funfood.R;
import com.example.funfood.databinding.ItemNotificationBinding;
import com.example.funfood.domain.model.Notification;
import com.example.funfood.util.DateUtil; // Giả sử bạn có class này
// import com.example.funfood.util.ImageUtil; // Dùng Glide/Picasso

import java.util.Objects;

public class NotificationAdapter extends ListAdapter<Notification, NotificationAdapter.NotificationViewHolder> {

    private final OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onItemClick(Notification notification);
        void onDeleteClick(Notification notification);
    }

    public NotificationAdapter(@NonNull OnNotificationClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new NotificationViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationBinding binding;
        private final Context context;

        public NotificationViewHolder(@NonNull ItemNotificationBinding binding, OnNotificationClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = binding.getRoot().getContext();

            binding.containerClickable.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });

            binding.ibDeleteNotification.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(getItem(position));
                }
            });
        }

        public void bind(Notification notification) {
            binding.tvNotificationTitle.setText(notification.getTitle());
            binding.tvNotificationMessage.setText(notification.getMessage());
            binding.tvNotificationTime.setText(DateUtil.getTimeAgo(notification.getCreatedAt())); // Dùng DateUtil

            // Xử lý trạng thái đã đọc/chưa đọc
            if (notification.isRead()) {
                binding.viewUnreadIndicator.setVisibility(View.GONE);
                binding.tvNotificationTitle.setTypeface(null, Typeface.NORMAL);
                binding.containerClickable.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            } else {
                binding.viewUnreadIndicator.setVisibility(View.VISIBLE);
                binding.tvNotificationTitle.setTypeface(null, Typeface.BOLD);
                binding.containerClickable.setBackgroundColor(ContextCompat.getColor(context, R.color.unread_notification_bg));
            }

            // Xử lý icon theo loại thông báo
            switch (notification.getType()) {
                case "order":
                    binding.ivNotificationIcon.setImageResource(R.drawable.ic_notification_order);
                    break;
                case "promotion":
                    binding.ivNotificationIcon.setImageResource(R.drawable.ic_notification_promotion);
                    break;
                default:
                    binding.ivNotificationIcon.setImageResource(R.drawable.ic_notification_system);
            }
        }
    }

    private static final DiffUtil.ItemCallback<Notification> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Notification>() {
                @Override
                public boolean areItemsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
                    // Dùng equals() đã override trong Model
                    return oldItem.equals(newItem);
                }
            };
}