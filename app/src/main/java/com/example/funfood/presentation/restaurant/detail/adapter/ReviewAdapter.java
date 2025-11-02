package com.example.funfood.presentation.restaurant.detail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funfood.R;
import com.example.funfood.domain.model.Review;
import com.example.funfood.util.DateUtil;
import com.example.funfood.util.ImageUtil;

public class ReviewAdapter extends ListAdapter<Review, ReviewAdapter.ReviewViewHolder> {

    public ReviewAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = getItem(position);
        if (review != null) {
            holder.bind(review);
        }
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivUserAvatar;
        private final TextView tvUserName;
        private final TextView tvReviewDate;
        private final TextView tvRating;
        private final TextView tvReviewComment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvReviewDate = itemView.findViewById(R.id.tv_review_date);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvReviewComment = itemView.findViewById(R.id.tv_review_comment);
        }

        public void bind(Review review) {
            // User name
            tvUserName.setText(review.getUserName() != null ?
                    review.getUserName() : "Người dùng ẩn danh");

            // Comment
            tvReviewComment.setText(review.getComment() != null ?
                    review.getComment() : "");

            // Date
            if (review.getCreatedAt() != null) {
                tvReviewDate.setText(DateUtil.getRelativeTime(review.getCreatedAt()));
            }

            // Rating
            tvRating.setText(String.valueOf(review.getRating()));

            // User avatar
            if (review.getUserAvatar() != null && !review.getUserAvatar().isEmpty()) {
                ImageUtil.loadCircularImage(
                        itemView.getContext(),
                        review.getUserAvatar(),
                        ivUserAvatar
                );
            } else {
                ivUserAvatar.setImageResource(R.drawable.ic_avatar_placeholder);
            }
        }
    }

    private static final DiffUtil.ItemCallback<Review> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Review>() {
                @Override
                public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
                    return oldItem.equals(newItem);
                }
            };
}