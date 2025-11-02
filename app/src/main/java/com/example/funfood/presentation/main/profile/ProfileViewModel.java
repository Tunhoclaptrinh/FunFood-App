package com.example.funfood.presentation.main.profile;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.funfood.data.preferences.UserPreferences;
import com.example.funfood.domain.model.User; // Import model User

// Sử dụng AndroidViewModel để có thể truy cập Context
public class ProfileViewModel extends AndroidViewModel {

    private final UserPreferences userPreferences;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        // Khởi tạo UserPreferences
        userPreferences = UserPreferences.getInstance(application.getApplicationContext());
        loadUser();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    private void loadUser() {
        // Lấy thông tin từ UserPreferences
        User user = new User();
        user.setId(userPreferences.getUserId());
        user.setName(userPreferences.getUserName());
        user.setEmail(userPreferences.getUserEmail());
        user.setRole(userPreferences.getUserRole());

        userLiveData.setValue(user);
    }

    public void logout() {
        userPreferences.logout();
        // (Thêm logic thông báo cho Fragment để điều hướng)
    }
}