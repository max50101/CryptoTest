package com.example.market_scan.di;

import com.example.market_scan.impl.MarketScanRepositoryImpl;
import com.example.market_scan.repository.MarketScanRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MarketScanModule {
    @Binds
    abstract MarketScanRepository repositoryProvider(MarketScanRepositoryImpl impl);
}
