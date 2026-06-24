package com.example.market_scan.mappers

import com.example.market_scan.model.MarketScanConfig
import com.example.scanner_api.MarketScanOptionsDto

fun MarketScanConfig.toDto(): MarketScanOptionsDto{
    return MarketScanOptionsDto(
        this.quoteAsset,
        this.interval,
        this.candlesLimit,
        this.maxSymbols
    )
}