package com.example.funfood;

import android.app.Application;

import com.example.funfood.data.preferences.UserPreferences;
import com.example.funfood.data.remote.RetrofitClient;

public class FunFoodApplication extends Application {

    private static FunFoodApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Initialize Retrofit
        RetrofitClient.getInstance(this);

        // Initialize SharedPreferences
        UserPreferences.getInstance(this);
    }

    public static FunFoodApplication getInstance() {
        return instance;
    }
}