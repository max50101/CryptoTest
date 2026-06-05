package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.entity.CoinsEntity
import com.example.database.features.CoinsDao

@Database(
    entities = [CoinsEntity::class],
    version = 1
)
abstract class AppDatabase(): RoomDatabase() {
    abstract fun coinsDao(): CoinsDao
}