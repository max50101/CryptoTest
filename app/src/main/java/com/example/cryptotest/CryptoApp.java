package com.example.cryptotest;

import android.app.Application;

import com.example.coins.CoinsFragment;
import com.example.coins.di.CoinsFeatureInjector;
import com.example.cryptotest.di.AppComponent;
import com.example.cryptotest.di.DaggerAppComponent;

import org.jetbrains.annotations.NotNull;

public class CryptoApp extends Application implements CoinsFeatureInjector {

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
}