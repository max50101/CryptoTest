package com.example.database.features

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entity.CoinsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinsDao {
    @Query("SELECT * FROM favoriteCoins")
    fun observeCoins(): Flow<List<CoinsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCoins(coin:List<CoinsEntity>)
}