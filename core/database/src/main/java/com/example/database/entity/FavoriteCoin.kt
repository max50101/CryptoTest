package com.example.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "favoriteCoins", indices = [Index("symbol")])
data class CoinsEntity(@PrimaryKey val symbol: String,
                       val name:String,
                       val imageUrl:String?,
                       val priceUsd: Double?)
