package com.example.cryptotest;

import android.app.Application;

import com.example.coin_alert.AlertBottomSheet;
import com.example.coin_alert.di.AlertBottomSheetFeatureInject;
import com.example.coin_details.CoinDetailsFragment;
import com.example.coin_details.di.CoinDetailsFeatureInjector;
import com.example.coin_list.CoinListFragment;
import com.example.coin_list.di.CoinsListFeatureInjector;
import com.example.coins.CoinsFragment;
import com.example.coins.di.CoinsFeatureInjector;
import com.example.cryptotest.di.AppComponent;
import com.example.cryptotest.di.DaggerAppComponent;

import com.example.feature.live_notification.NotificationForegroundService;
import com.example.live_notification.di.NotificationInjector;

import org.jetbrains.annotations.NotNull;

public class CryptoApp extends Application implements CoinsFeatureInjector,
        CoinsListFeatureInjector,
        CoinDetailsFeatureInjector,
        AlertBottomSheetFeatureInject,
        NotificationInjector {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .factory()
                .create(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void inject(@NotNull CoinsFragment fragment) {
        appComponent.inject(fragment);
    }

    @Override
    public void inject(@NotNull CoinListFragment fragment) {
        appComponent.inject(fragment);
    }

    @Override
    public void inject(@NotNull CoinDetailsFragment fragment) {
        appComponent.inject(fragment);
    }

    @Override
    public void inject(@NotNull AlertBottomSheet fragment) {
        appComponent.inject(fragment);
    }

    @Override
    public void inject(@NotNull NotificationForegroundService service){
        appComponent.inject(service);
    }
}