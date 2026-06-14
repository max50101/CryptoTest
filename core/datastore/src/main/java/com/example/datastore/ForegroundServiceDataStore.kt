package com.example.datastore

import android.app.Application
import androidx.datastore.preferences.core.edit
import com.example.datastore.keys.CryptoPreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ForegroundServiceDataStore @Inject constructor(private val application: Application) {
    suspend fun  setCurrentServiceSymbol(symbol:String){
        application.dataStore.edit { preferences ->
            preferences[CryptoPreferencesKeys.COINS_CURRENT_FOREGROUND_SERVICE_SYMBOL]=symbol
        }
    }

     fun getCurrentServiceSymbol(): Flow<String> {
        return application.dataStore.data.map { it.get(CryptoPreferencesKeys.COINS_CURRENT_FOREGROUND_SERVICE_SYMBOL)?:"" }
    }

    suspend fun clearActiveSymbol() {
        application.dataStore.edit { preferences ->
            preferences.remove(CryptoPreferencesKeys.COINS_CURRENT_FOREGROUND_SERVICE_SYMBOL)
        }
    }



}