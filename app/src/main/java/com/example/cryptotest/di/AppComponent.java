package com.example.cryptotest.di;

import android.app.Application;

import com.example.alerts.di.AlertsDataModule;
import com.example.coin_alert.AlertBottomSheet;
import com.example.coin_details.CoinDetailsFragment;
import com.example.coin_list.CoinListFragment;
import com.example.coins.CoinsFragment;
import com.example.data.coins.di.CoinsDataModule;
import com.example.database.di.DatabaseModule;

import com.example.feature.live_notification.NotificationForegroundService;
import com.example.live_notification.di.LiveNotificationModule;
import com.example.network.di.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(
        modules = {
            NetworkModule.class,
                CoinsDataModule.class,
                DatabaseModule.class,
                AppNavigatorModule.class,
                AlertsDataModule.class,
                LiveNotificationModule.class
        }
)
public interface AppComponent {

    void inject(CoinsFragment fragment);
    void inject(CoinListFragment fragment);
    void inject(CoinDetailsFragment fragment);

    void inject(AlertBottomSheet fragment);

    void inject(NotificationForegroundService service);

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application);
    }
}