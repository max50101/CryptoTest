package com.example.database.features

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entity.AlertEntity
import com.example.database.entity.AlertStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao{
    @Query("SELECT * FROM alerts WHERE status = :status")
    fun observeAlertsByStatus(status: AlertStatusEntity): Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts WHERE status = :status")
    suspend fun getAlertsByStatus(status: AlertStatusEntity): List<AlertEntity>

    @Query("SELECT * FROM alerts WHERE symbol = :symbol AND status=:status ORDER BY createdAt DESC")
    fun observeAlertsBySymbol(symbol: String,status: AlertStatusEntity): Flow<List<AlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alertEntity: AlertEntity): Long

    @Query("UPDATE alerts SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: AlertStatusEntity)

    @Delete
    suspend fun deleteAlert(alertEntity: AlertEntity)
}