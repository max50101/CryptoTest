package com.example.network.di;

import com.example.network.NetworkSettings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofit(
            OkHttpClient okHttpClient
    ) {
        return new Retrofit.Builder()
                .baseUrl(NetworkSettings.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}