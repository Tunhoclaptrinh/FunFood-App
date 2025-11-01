package com.example.funfood.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.funfood.util.Constants;

public class UserPreferences {

    private static UserPreferences instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized UserPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new UserPreferences(context.getApplicationContext());
        }
        return instance;
    }

    // Auth Token
    public void saveToken(String token) {
        editor.putString(Constants.KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(Constants.KEY_TOKEN, null);
    }

    public void clearToken() {
        editor.remove(Constants.KEY_TOKEN);
        editor.apply();
    }

    // User Info
    public void saveUserInfo(int userId, String email, String name, String role) {
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USER_EMAIL, email);
        editor.putString(Constants.KEY_USER_NAME, name);
        editor.putString(Constants.KEY_USER_ROLE, role);
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(Constants.KEY_USER_ID, -1);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(Constants.KEY_USER_EMAIL, null);
    }

    public String getUserName() {
        return sharedPreferences.getString(Constants.KEY_USER_NAME, null);
    }

    public String getUserRole() {
        return sharedPreferences.getString(Constants.KEY_USER_ROLE, "customer");
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public boolean isAdmin() {
        return "admin".equals(getUserRole());
    }

    // Logout
    public void logout() {
        editor.clear();
        editor.apply();
    }

    // Generic methods
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}