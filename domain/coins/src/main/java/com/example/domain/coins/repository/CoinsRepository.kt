package com.example.domain.coins.repository

import com.example.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {
    fun observeFavoriteCoins(): Flow<List<Coin>>
    fun observeCoinDetails(id:String): Flow<Coin>

    fun observeCoins():Flow<List<Coin>>

    suspend fun refreshCoins()
    suspend fun toggleFavorite(symbol:String)
}