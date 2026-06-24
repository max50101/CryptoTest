package com.example.scan_engine.service.retrofit.model

data class Ticker24hDto(
    val symbol: String,
    val priceChangePercent: String,
    val quoteVolume: String
)
