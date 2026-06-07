package com.example.cryptotest;

import android.app.Application;

import com.example.coin_list.CoinListFragment;
import com.example.coin_list.di.CoinsListFeatureInjector;
import com.example.coins.CoinsFragment;
import com.example.coins.di.CoinsFeatureInjector;
import com.example.cryptotest.di.AppComponent;
import com.example.cryptotest.di.DaggerAppComponent;

import org.jetbrains.annotations.NotNull;

public class CryptoApp extends Application implements CoinsFeatureInjector, CoinsListFeatureInjector {

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
    public void inject(@NotNull CoinListFragment fragment){appComponent.inject(fragment);}
}