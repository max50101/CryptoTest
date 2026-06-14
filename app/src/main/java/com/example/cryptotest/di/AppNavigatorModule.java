package com.example.cryptotest.di;

import com.example.cryptotest.navigation.AppNavigatorImplementation;
import com.example.navigation.AppNavigator;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AppNavigatorModule {
    @Binds
    abstract AppNavigator bindAppNavigator(AppNavigatorImplementation impl);
}
