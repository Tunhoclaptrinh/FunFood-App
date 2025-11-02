package com.example.funfood.presentation.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
// Giả sử bạn đang dùng ViewBinding và tệp layout của bạn là fragment_profile.xml
import com.example.funfood.databinding.FragmentProfileBinding;
import com.example.funfood.data.preferences.UserPreferences; // Import lớp UserPreferences

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private UserPreferences userPreferences; // Thêm UserPreferences

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Sử dụng ViewBinding để inflate layout
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo UserPreferences
        userPreferences = UserPreferences.getInstance(requireContext());

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Lấy thông tin người dùng từ SharedPreferences và hiển thị
        // Đây là lý do tại sao ProfileViewModel cần UserPreferences
        loadUserProfile();

        // (Thêm logic cho các nút bấm, ví dụ: nút Đăng xuất)
        binding.buttonLogout.setOnClickListener(v -> {
            userPreferences.logout();
            // (Thêm logic chuyển về màn hình Login)
        });
    }

    private void loadUserProfile() {
        // Lấy dữ liệu trực tiếp từ UserPreferences
        String name = userPreferences.getUserName();
        String email = userPreferences.getUserEmail();

        // Hiển thị lên TextView (giả sử bạn có tvName và tvEmail trong fragment_profile.xml)
        binding.tvName.setText(name);
        binding.tvEmail.setText(email);

        // (Bạn cũng có thể di chuyển logic này vào ViewModel nếu muốn)
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Tránh rò rỉ bộ nhớ
    }
}