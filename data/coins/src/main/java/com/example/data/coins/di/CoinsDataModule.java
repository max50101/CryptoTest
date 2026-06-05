package com.example.data.coins.di;

import com.example.data.coins.api.CoinsApi;
import com.example.data.coins.api.websocket.BinanceWebSocket;
import com.example.data.coins.impl.CoinsRepositoryImp;
import com.example.domain.coins.repository.CoinsRepository;


import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
public abstract class CoinsDataModule {

    @Provides
    static CoinsApi coinsApi(Retrofit retrofit){
        return retrofit.create(CoinsApi.class);
    }

    @Provides
    static BinanceWebSocket binanceWebSocket(OkHttpClient client){return  new BinanceWebSocket(client);}
    @Binds
    abstract CoinsRepository bindCoinRepository(
            CoinsRepositoryImp imp
    );
}
