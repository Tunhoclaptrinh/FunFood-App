package com.example.funfood.di;

import androidx.test.espresso.core.internal.deps.dagger.Provides;

import com.example.funfood.data.remote.api.NotificationApi;

import javax.inject.Singleton;

import retrofit2.Retrofit;

public class NetworkModule {

    // ... (bên trong class NetworkModule)

    @Provides
    @Singleton
    public NotificationApi provideNotificationApi(Retrofit retrofit) {
        return retrofit.create(NotificationApi.class);
    }
}
