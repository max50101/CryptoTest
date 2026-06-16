package com.example.alerts.impl

import android.util.Log
import com.example.alerts.repository.MarketPriceRepository
import com.example.network.api.CoinsApi
import com.example.network.safeApiCall
import javax.inject.Inject

class MarketPriceRepositoryImpl @Inject constructor(private val coinsApi: CoinsApi): MarketPriceRepository {
    override suspend fun getCurrentPrices(symbols: Set<String>): Map<String, Double> {
        val currentPrices=  coinsApi.getCoinsPriceBySymbol( symbols.joinToString(
            separator = ",",
            prefix = "[",
            postfix = "]"
        ) { "\"$it\"" })
        return currentPrices.associate { it.symbol.uppercase() to it.price }
    }
}