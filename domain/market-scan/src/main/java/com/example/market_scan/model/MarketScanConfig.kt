package com.example.market_scan.model

data class MarketScanConfig(
    val quoteAsset: String = "USDT",
    val interval: String = "15m",
    val candlesLimit: Int = 100,
    val maxSymbols: Int = 100,
    val minVolumeSpikeMultiplier: Double = 2.0,
    val minPriceChangePercent: Double = 2.5
)