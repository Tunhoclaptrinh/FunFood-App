package com.example.funfood.presentation.main.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.preferences.UserPreferences;
import com.example.funfood.data.repository.AuthRepository;
import com.example.funfood.domain.model.User;
import com.example.funfood.presentation.base.BaseViewModel;
import com.example.funfood.util.Resource;
import com.example.funfood.util.SingleEvent;

public class ProfileViewModel extends BaseViewModel {

    private final UserPreferences userPreferences;
    private final AuthRepository authRepository;

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<SingleEvent<Boolean>> logoutEvent = new MutableLiveData<>();
    private final MutableLiveData<Resource<User>> updateProfileResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<Void>> changePasswordResult = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
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

    public LiveData<Resource<User>> getUpdateProfileResult() {
        return updateProfileResult;
    }

    public LiveData<Resource<Void>> getChangePasswordResult() {
        return changePasswordResult;
    }

    /**
     * Load user info from cache and refresh from API
     */
    private void loadUser() {
        // Get user from SharedPreferences first (offline)
        User user = new User();
        user.setId(userPreferences.getUserId());
        user.setName(userPreferences.getUserName());
        user.setEmail(userPreferences.getUserEmail());
        user.setRole(userPreferences.getUserRole());

        userLiveData.setValue(user);

        // Optionally: Fetch from API to get latest data
        refreshUserFromApi();
    }

    /**
     * Refresh user data from API
     */
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
                        setError("Không thể tải thông tin mới nhất");
                        break;
                }
            }
        });
    }

    /**
     * Update user profile
     */
    public void updateProfile(String name, String phone, String address, String avatar) {
        updateProfileResult.setValue(Resource.loading(null));

        // Validate
        if (name == null || name.trim().isEmpty()) {
            updateProfileResult.setValue(Resource.error("Tên không được để trống", null));
            return;
        }

        if (phone != null && !phone.trim().isEmpty() && !isValidPhone(phone)) {
            updateProfileResult.setValue(Resource.error("Số điện thoại không hợp lệ", null));
            return;
        }

        // Call repository to update profile
        authRepository.updateProfile(name, phone, address, avatar).observeForever(resource -> {
            updateProfileResult.setValue(resource);

            if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                // Update local cache
                userLiveData.setValue(resource.getData());
                userPreferences.saveUserInfo(
                        resource.getData().getId(),
                        resource.getData().getEmail(),
                        resource.getData().getName(),
                        resource.getData().getRole()
                );
                setSuccess("Cập nhật thông tin thành công");
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                setError(resource.getMessage());
            }
        });
    }

    /**
     * Change password
     */
    public void changePassword(String currentPassword, String newPassword) {
        changePasswordResult.setValue(Resource.loading(null));

        // Validate
        if (currentPassword == null || currentPassword.isEmpty()) {
            changePasswordResult.setValue(Resource.error("Mật khẩu hiện tại không được để trống", null));
            return;
        }

        if (newPassword == null || newPassword.length() < 6) {
            changePasswordResult.setValue(Resource.error("Mật khẩu mới phải có ít nhất 6 ký tự", null));
            return;
        }

        if (currentPassword.equals(newPassword)) {
            changePasswordResult.setValue(Resource.error("Mật khẩu mới phải khác mật khẩu cũ", null));
            return;
        }

        // Call repository
        authRepository.changePassword(currentPassword, newPassword).observeForever(resource -> {
            changePasswordResult.setValue(resource);

            if (resource.getStatus() == Resource.Status.SUCCESS) {
                setSuccess("Đổi mật khẩu thành công");
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                setError(resource.getMessage());
            }
        });
    }

    /**
     * Logout
     */
    public void logout() {
        setLoading(true);

        // Clear local data
        userPreferences.logout();

        setLoading(false);
        setSuccess("Đăng xuất thành công");

        // Trigger logout event
        logoutEvent.setValue(new SingleEvent<>(true));
    }

    /**
     * Refresh profile manually
     */
    public void refreshProfile() {
        setLoading(true);
        refreshUserFromApi();
        setLoading(false);
    }

    /**
     * Get user statistics
     */
    public void loadUserStats() {
        // TODO: Implement when backend API is ready
        // This would fetch user's order count, total spent, etc.
    }

    /**
     * Validate phone number
     */
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        return cleanPhone.matches("^0[0-9]{9,10}$");
    }

    /**
     * Clear update profile result
     */
    public void clearUpdateProfileResult() {
        updateProfileResult.setValue(null);
    }

    /**
     * Clear change password result
     */
    public void clearChangePasswordResult() {
        changePasswordResult.setValue(null);
    }
}