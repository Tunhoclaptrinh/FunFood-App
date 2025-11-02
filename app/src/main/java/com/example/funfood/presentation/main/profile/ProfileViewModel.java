package com.example.funfood.presentation.main.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.preferences.UserPreferences;
import com.example.funfood.data.repository.AuthRepository;
import com.example.funfood.domain.model.User;
import com.example.funfood.presentation.base.BaseViewModel;
import com.example.funfood.util.SingleEvent;

public class ProfileViewModel extends BaseViewModel {

    private final UserPreferences userPreferences;
    private final AuthRepository authRepository;

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<SingleEvent<Boolean>> logoutEvent = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super();
        userPreferences = UserPreferences.getInstance(application.getApplicationContext());
        authRepository = new AuthRepository(application.getApplicationContext());
        loadUser();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<SingleEvent<Boolean>> getLogoutEvent() {
        return logoutEvent;
    }

    private void loadUser() {
        setLoading(true);

        // Get user from SharedPreferences first (offline)
        User user = new User();
        user.setId(userPreferences.getUserId());
        user.setName(userPreferences.getUserName());
        user.setEmail(userPreferences.getUserEmail());
        user.setRole(userPreferences.getUserRole());

        userLiveData.setValue(user);
        setLoading(false);

        // Optionally: Fetch from API to get latest data
        refreshUserFromApi();
    }

    private void refreshUserFromApi() {
        authRepository.getProfile().observeForever(resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        if (resource.getData() != null) {
                            userLiveData.setValue(resource.getData());

                            // Update SharedPreferences
                            userPreferences.saveUserInfo(
                                    resource.getData().getId(),
                                    resource.getData().getEmail(),
                                    resource.getData().getName(),
                                    resource.getData().getRole()
                            );
                        }
                        break;

                    case ERROR:
                        // Silent fail - we already have cached data
                        break;
                }
            }
        });
    }

    public void logout() {
        setLoading(true);

        // Call API logout (optional)
        // authRepository.logout();

        // Clear local data
        userPreferences.logout();

        setLoading(false);
        setSuccess("Đăng xuất thành công");

        // Trigger logout event
        logoutEvent.setValue(new SingleEvent<>(true));
    }

    public void refreshProfile() {
        loadUser();
    }
}