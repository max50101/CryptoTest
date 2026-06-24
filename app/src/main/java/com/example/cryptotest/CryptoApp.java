package com.example.cryptotest;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Configuration;

import com.example.alert_worker.BootReceiver;
import com.example.alert_worker.di.BootReceiverBackground;
import com.example.alert_worker.factory.AppWorkerFactory;
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
import com.example.market_scan.MarketScanFragment;
import com.example.market_scan.di.MarketScanInjector;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class CryptoApp extends Application implements CoinsFeatureInjector,
        CoinsListFeatureInjector,
        CoinDetailsFeatureInjector,
        AlertBottomSheetFeatureInject,
        BootReceiverBackground,
        NotificationInjector, MarketScanInjector, Configuration.Provider {

    private AppComponent appComponent;

    @Inject
    AppWorkerFactory appWorkerFactory;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .factory()
                .create(this);
        appComponent.inject(this);
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

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        Log.d("CryptoAPP","Work Manager configuration");
        return new Configuration.Builder()
                .setWorkerFactory(appWorkerFactory)
                .build();
    }

    @Override
    public void inject(@NotNull BootReceiver bootReceiver) {
        appComponent.inject(bootReceiver);
    }

    @Override
    public void inject(@NotNull MarketScanFragment fragment) {
        appComponent.inject(fragment);
    }
}