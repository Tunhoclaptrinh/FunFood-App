package com.example.funfood.presentation.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VB extends ViewBinding> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder<VB>> {

    protected List<T> items = new ArrayList<>();
    protected OnItemClickListener<T> onItemClickListener;

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<VB> holder, int position) {
        T item = items.get(position);
        bind(holder.binding, item, position);

        // Handle item click
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v ->
                    onItemClickListener.onItemClick(item, position)
            );
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Bind data to view
     */
    protected abstract void bind(VB binding, T item, int position);

    /**
     * Update data
     */
    public void setItems(List<T> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        if (items != null && !items.isEmpty()) {
            int startPosition = this.items.size();
            this.items.addAll(items);
            notifyItemRangeInserted(startPosition, items.size());
        }
    }

    public void addItem(T item) {
        if (item != null) {
            this.items.add(item);
            notifyItemInserted(this.items.size() - 1);
        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateItem(int position, T item) {
        if (position >= 0 && position < items.size() && item != null) {
            items.set(position, item);
            notifyItemChanged(position);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return items;
    }

    public T getItem(int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    /**
     * Click listener
     */
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    /**
     * Base ViewHolder
     */
    public static class BaseViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
        public final VB binding;

        public BaseViewHolder(@NonNull VB binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}