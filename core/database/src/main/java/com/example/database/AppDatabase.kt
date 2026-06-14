package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.database.entity.AlertEntity
import com.example.database.entity.CoinsEntity
import com.example.database.features.AlertsDao
import com.example.database.features.CoinsDao
import com.example.database.utils.AlertTypeConverter

@Database(
    entities = [CoinsEntity::class, AlertEntity::class],
    version = 1
)
@TypeConverters(AlertTypeConverter::class)
abstract class AppDatabase(): RoomDatabase() {
    abstract fun coinsDao(): CoinsDao
    abstract fun alertsDao(): AlertsDao
}