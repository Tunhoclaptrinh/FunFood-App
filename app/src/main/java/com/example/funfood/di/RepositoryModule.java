package com.example.funfood.di;

import androidx.test.espresso.core.internal.deps.dagger.Provides;

import com.example.funfood.data.remote.api.NotificationApi;
import com.example.funfood.data.repository.NotificationRepository;

import javax.inject.Singleton;

public class RepositoryModule {

    @Provides
    @Singleton
    public NotificationRepository provideNotificationRepository(NotificationApi api) {
        return new NotificationRepository(api);
    }
}
