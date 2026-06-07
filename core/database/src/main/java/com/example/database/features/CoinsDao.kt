package com.example.database.features

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.database.entity.CoinsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinsDao {
    @Query("SELECT * FROM coins WHERE isFavorite=1")
    fun observeFavoriteCoins(): Flow<List<CoinsEntity>>

    @Query("SELECT * FROM coins WHERE status='TRADING' ORDER BY isFavorite DESC")
    fun observeCoins():Flow<List<CoinsEntity>>
    @Query("""INSERT INTO coins(symbol, baseAsset, quoteAsset, priceUsd,  status, isFavorite)
            VALUES(:symbol,:baseAsset,:quoteAsset,:priceUsd,:status, 0 ) 
            ON CONFLICT(symbol) DO UPDATE SET 
            baseAsset=excluded.baseAsset,
            quoteAsset=excluded.quoteAsset,
            priceUsd=excluded.priceUsd,
            status=excluded.status
            """)
    suspend fun updateCoinKeepingFavorite(symbol:String,baseAsset:String,
                                          quoteAsset:String,priceUsd:Double?, status:String)

    @Transaction
    suspend fun updateCoinsKeepingFavorite(coins:List<CoinsEntity>){
        coins.forEach { entity->
            updateCoinKeepingFavorite(entity.symbol,
                entity.baseAsset,
                entity.quoteAsset,entity.priceUsd,entity.status)
        }
    }
    @Upsert()
    suspend fun upsertCoins(coin:List<CoinsEntity>)

    @Query(""" UPDATE coins SET isFavorite=NOT isFavorite WHERE symbol=:symbol""")
    suspend fun updateCoin(symbol: String)
}