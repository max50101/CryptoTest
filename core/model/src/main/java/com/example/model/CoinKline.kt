package com.example.model

data class CoinKline(
    val symbol: String,
    val interval: String,
    val openPrice: Double,
    val closePrice: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val isClosed: Boolean,
    val openTime:Long,
    val closeTime:Long
)