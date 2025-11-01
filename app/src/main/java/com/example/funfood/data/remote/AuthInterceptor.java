package com.example.funfood.data.remote;

import android.content.Context;

import com.example.funfood.data.preferences.UserPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final UserPreferences userPreferences;

    public AuthInterceptor(Context context) {
        this.userPreferences = UserPreferences.getInstance(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Skip auth for login/register endpoints
        String url = originalRequest.url().toString();
        if (url.contains("/auth/login") || url.contains("/auth/register")) {
            return chain.proceed(originalRequest);
        }

        // Add JWT token to header
        String token = userPreferences.getToken();

        if (token != null && !token.isEmpty()) {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(originalRequest);
    }
}