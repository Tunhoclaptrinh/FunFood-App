package com.example.funfood.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.preferences.UserPreferences;
import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.AuthApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.remote.dto.request.LoginRequest;
import com.example.funfood.data.remote.dto.request.RegisterRequest;
import com.example.funfood.data.remote.dto.response.LoginResponse;
import com.example.funfood.data.remote.dto.response.UserResponse;
import com.example.funfood.domain.model.User;
import com.example.funfood.util.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthApi authApi;
    private final UserPreferences userPreferences;

    public AuthRepository(Context context) {
        this.authApi = RetrofitClient.getInstance(context).createService(AuthApi.class);
        this.userPreferences = UserPreferences.getInstance(context);
    }

    // Login
    public LiveData<Resource<User>> login(String email, String password) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        LoginRequest request = new LoginRequest(email, password);

        authApi.login(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call,
                                   Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginResponse> apiResponse = response.body();

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        LoginResponse loginResponse = apiResponse.getData();
                        UserResponse userResponse = loginResponse.getUser();
                        String token = loginResponse.getToken();

                        // Save to SharedPreferences
                        userPreferences.saveToken(token);
                        userPreferences.saveUserInfo(
                                userResponse.getId(),
                                userResponse.getEmail(),
                                userResponse.getName(),
                                userResponse.getRole()
                        );

                        // Convert to Domain Model
                        User user = convertToUser(userResponse);
                        result.setValue(Resource.success(user));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Login failed", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    // Register
    public LiveData<Resource<User>> register(String email, String password, String name,
                                             String phone, String address) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        RegisterRequest request = new RegisterRequest(email, password, name, phone, address);

        authApi.register(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call,
                                   Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginResponse> apiResponse = response.body();

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        LoginResponse loginResponse = apiResponse.getData();
                        UserResponse userResponse = loginResponse.getUser();
                        String token = loginResponse.getToken();

                        // Save to SharedPreferences
                        userPreferences.saveToken(token);
                        userPreferences.saveUserInfo(
                                userResponse.getId(),
                                userResponse.getEmail(),
                                userResponse.getName(),
                                userResponse.getRole()
                        );

                        // Convert to Domain Model
                        User user = convertToUser(userResponse);
                        result.setValue(Resource.success(user));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Registration failed", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    // Get Profile
    public LiveData<Resource<User>> getProfile() {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        authApi.getProfile().enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call,
                                   Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserResponse> apiResponse = response.body();

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        User user = convertToUser(apiResponse.getData());
                        result.setValue(Resource.success(user));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to get profile", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    // Logout
    public void logout() {
        userPreferences.logout();
    }

    // Helper: Convert UserResponse to User
    private User convertToUser(UserResponse userResponse) {
        return new User(
                userResponse.getId(),
                userResponse.getEmail(),
                userResponse.getName(),
                userResponse.getPhone(),
                userResponse.getAddress(),
                userResponse.getAvatar(),
                userResponse.getRole(),
                userResponse.isActive(),
                userResponse.getCreatedAt(),
                userResponse.getLastLogin()
        );
    }

    // Check if logged in
    public boolean isLoggedIn() {
        return userPreferences.isLoggedIn();
    }

    /**
     * Update user profile
     */
    public LiveData<Resource<User>> updateProfile(String name, String phone, String address, String avatar) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        UpdateProfileRequest request = new UpdateProfileRequest(name, phone, address, avatar);

        authApi.updateProfile(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call,
                                   Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<UserResponse> apiResponse = response.body();

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        User user = convertToUser(apiResponse.getData());
                        result.setValue(Resource.success(user));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to update profile", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Change password
     */
    public LiveData<Resource<Void>> changePassword(String currentPassword, String newPassword) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        AuthApi.ChangePasswordRequest request = new AuthApi.ChangePasswordRequest(currentPassword, newPassword);

        authApi.changePassword(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call,
                                   Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(null));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Failed to change password", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    // Request class for update profile
    public static class UpdateProfileRequest {
        private String name;
        private String phone;
        private String address;
        private String avatar;

        public UpdateProfileRequest(String name, String phone, String address, String avatar) {
            this.name = name;
            this.phone = phone;
            this.address = address;
            this.avatar = avatar;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
    }
}