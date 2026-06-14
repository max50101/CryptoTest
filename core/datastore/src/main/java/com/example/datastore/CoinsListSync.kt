package com.example.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.datastore.keys.CryptoPreferencesKeys
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

import javax.inject.Inject
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("crypto_preferences")

class CoinsListSync @Inject constructor(private val application: Application) {
    suspend fun shallUpdateCoins(): Boolean{
        val timeToUpdate=application.dataStore.data.map {
            it.get(CryptoPreferencesKeys.COINS_LAST_UPDATED_MILLIS)?:0L
        }.first()
        return System.currentTimeMillis()-timeToUpdate> COINS_REFRESH_INTERVAL_MS

    }

    suspend fun setUpdateTime(){
        application.dataStore.edit {preferences ->
            preferences[CryptoPreferencesKeys.COINS_LAST_UPDATED_MILLIS]= System.currentTimeMillis()
        }
        }


    companion object{
        private const val COINS_REFRESH_INTERVAL_MS=24*60*60*1000L
    }
}