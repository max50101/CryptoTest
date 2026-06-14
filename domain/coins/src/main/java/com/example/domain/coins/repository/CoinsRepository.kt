package com.example.domain.coins.repository

import com.example.model.Coin
import com.example.model.CoinKline
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {
    fun observeFavoriteCoins(): Flow<List<Coin>>
    fun observeCoinChart(symbol:String, interval:String): Flow<List<CoinKline>>

    fun observeCoins():Flow<List<Coin>>

    fun observeCoinDetails(symbol:String):Flow<Coin>

    fun observeCoinPrice(symbol:String):Flow<Coin>

    suspend fun refreshCoins()
    suspend fun toggleFavorite(symbol:String)
}