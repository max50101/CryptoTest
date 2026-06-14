package com.example.live_notification.di

import com.example.feature.live_notification.NotificationForegroundService


interface NotificationInjector {
    fun inject(service: NotificationForegroundService)
}