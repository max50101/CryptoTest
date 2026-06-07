package com.example.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "coins")

data class CoinsEntity(

    @PrimaryKey val symbol: String,

    val baseAsset: String,

    val quoteAsset: String,

    val priceUsd: Double?,

    val status: String,

    val isFavorite: Boolean = false

)
