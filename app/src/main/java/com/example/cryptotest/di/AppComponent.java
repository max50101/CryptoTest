package com.example.cryptotest.di;

import android.app.Application;

import com.example.coins.CoinsFragment;
import com.example.data.coins.di.CoinsDataModule;
import com.example.database.di.DatabaseModule;
import com.example.network.di.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(
        modules = {
            NetworkModule.class,
                CoinsDataModule.class,
                DatabaseModule.class
        }
)
public interface AppComponent {

    void inject(CoinsFragment fragment);

    @Component.Factory
    interface Factory {

        AppComponent create(@BindsInstance Application application);
    }
}