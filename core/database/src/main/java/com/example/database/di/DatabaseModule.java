package com.example.database.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.database.AppDatabase;
import com.example.database.features.CoinsDao;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import kotlin.SinceKotlin;

@Module
public class DatabaseModule {

    @Singleton
    @Provides
    static AppDatabase provideAppDatabase(Application application){
        return Room.databaseBuilder(application, AppDatabase.class,"crypto.db")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        db.execSQL("INSERT INTO favoriteCoins(symbol,name,imageUrl,priceUsd) VALUES('BTC','Bitcoin',NULL,NULL)");
                        db.execSQL("INSERT INTO favoriteCoins(symbol,name,imageUrl,priceUsd) VALUES('ETH','Ethereum',NULL,NULL)");
                        db.execSQL("INSERT INTO favoriteCoins(symbol,name,imageUrl,priceUsd) VALUES('SOL','Solana',NULL,NULL)");
                    }
                })
                .build();
    }
    @Singleton
    @Provides
    static CoinsDao provideCoinsDao(AppDatabase appDatabase){
        return appDatabase.coinsDao();
    }
}
