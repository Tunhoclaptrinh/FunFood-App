package com.example.funfood.presentation.main.favorite;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.funfood.databinding.FragmentFavoriteBinding;
import com.example.funfood.presentation.base.BaseFragment;

public class FavoriteFragment extends BaseFragment<FragmentFavoriteBinding> {

    @Override
    protected FragmentFavoriteBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFavoriteBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        binding.tvFavoriteTitle.setText("Yêu thích");
    }

    @Override
    protected void observeData() {
        // TODO: Observe ViewModel
    }
}