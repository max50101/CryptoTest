package com.example.market_scan.repository

import com.example.market_scan.model.MarketScanConfig
import com.example.market_scan.model.MarketScanState
import com.example.market_scan.model.ScannerConnectionState
import kotlinx.coroutines.flow.Flow

interface MarketScanRepository {
    fun connectionState(): Flow<ScannerConnectionState>

    fun scanState(): Flow<MarketScanState>

    fun connect()

    fun disconnect()

    fun startScan(config: MarketScanConfig)

    fun cancelCurrentScan()
}