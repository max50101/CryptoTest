package com.example.alerts.di;

import com.example.alerts.impl.AlertsRepositoryImpl;

import com.example.alerts.impl.MarketPriceRepositoryImpl;
import com.example.alerts.repository.AlertsRepository;
import com.example.alerts.repository.MarketPriceRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AlertsDataModule {
    @Binds
    abstract AlertsRepository bindAlertsRepository(AlertsRepositoryImpl impl);

    @Binds
    abstract com.example.alerts.repository.MarketPriceRepository bindMarketPriceRepository(MarketPriceRepositoryImpl impl);
}
