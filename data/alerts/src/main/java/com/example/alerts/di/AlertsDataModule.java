package com.example.alerts.di;

import com.example.alerts.impl.AlertsRepositoryImpl;
import com.example.alerts.repository.AlertsRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AlertsDataModule {
    @Binds
    abstract AlertsRepository bindAlertsRepository(AlertsRepositoryImpl impl);
}
