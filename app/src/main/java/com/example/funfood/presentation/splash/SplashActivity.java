package com.example.funfood.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.funfood.R;
import com.example.funfood.data.preferences.UserPreferences;
import com.example.funfood.presentation.auth.LoginActivity;
import com.example.funfood.presentation.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Splash không cần layout phức tạp, có thể dùng theme
        // setContentView(R.layout.activity_splash);

        // Delay and check login status
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkLoginStatus();
        }, SPLASH_DELAY);
    }

    private void checkLoginStatus() {
        UserPreferences prefs = UserPreferences.getInstance(this);

        Intent intent;
        if (prefs.isLoggedIn()) {
            // Navigate to Main
            intent = new Intent(this, MainActivity.class);
        } else {
            // Navigate to Login
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}