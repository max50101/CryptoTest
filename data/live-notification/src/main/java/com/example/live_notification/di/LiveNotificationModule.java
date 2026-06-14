package com.example.live_notification.di;

import com.example.live_notification.impl.LiveNotificationRepositoryImpl;
import com.example.live_notification.repository.LiveNotificationRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LiveNotificationModule {
    @Binds
    abstract LiveNotificationRepository getLiveNotificationRepo(LiveNotificationRepositoryImpl impl);
}
