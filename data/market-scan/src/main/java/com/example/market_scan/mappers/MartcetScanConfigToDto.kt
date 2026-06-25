package com.example.market_scan.mappers

import com.example.market_scan.model.MarketScanConfig
import com.example.market_scan.model.MarketSignal
import com.example.market_scan.model.SignalType
import com.example.scanner_api.MarketScanOptionsDto
import com.example.scanner_api.MarketSignalDto

fun MarketScanConfig.toDto(): MarketScanOptionsDto{
    return MarketScanOptionsDto(
        this.quoteAsset,
        this.interval,
        this.candlesLimit,
        this.maxSymbols
    )
}

fun MarketSignalDto.toDomain(): MarketSignal{
    return MarketSignal(symbol,type.toSignalType(priceChangePercent),description,score,priceChangePercent,volumeSpikeMultiplier)
}

private fun String.toSignalType(priceChangePercent: Double): SignalType {
    return when (this) {
        "VOLUME_SPIKE" -> SignalType.VolumeSpike
        "PRICE_CHANGE_24H" -> if (priceChangePercent < 0.0) SignalType.PriceDump else SignalType.PricePump
        "PRICE_PUMP" -> SignalType.PricePump
        "PRICE_DUMP" -> SignalType.PriceDump
        "BREAKOUT" -> SignalType.Breakout
        "HIGH_VOLATILITY" -> SignalType.HighVolatility
        "RSI"-> SignalType.RSI
        "RSI_SPIKE" -> SignalType.RSI
        else -> SignalType.UNKNOWN
    }
}
