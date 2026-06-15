package com.example.datastore.keys

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object CryptoPreferencesKeys {
    val COINS_LAST_UPDATED_MILLIS= longPreferencesKey("coins_last_updated_millis")
    val COINS_CURRENT_FOREGROUND_SERVICE_SYMBOL= stringPreferencesKey("foreground_pirce_symbol")
}