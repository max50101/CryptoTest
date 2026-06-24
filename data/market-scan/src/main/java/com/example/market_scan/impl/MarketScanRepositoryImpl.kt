package com.example.market_scan.impl

import android.app.Application
import android.net.IpConfiguration
import com.example.market_scan.ipc.IpcConnectionMarketScan
import com.example.market_scan.model.MarketScanConfig
import com.example.market_scan.model.MarketScanState
import com.example.market_scan.model.ScannerConnectionState
import com.example.market_scan.repository.MarketScanRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarketScanRepositoryImpl @Inject constructor (private val marketScan: IpcConnectionMarketScan) : MarketScanRepository {
    override fun connectionState(): Flow<ScannerConnectionState> {
        return marketScan.connectionState
    }

    override fun scanState(): Flow<MarketScanState> {
        return marketScan.marketScanState
    }

    override fun connect() {
        marketScan.bind()
    }

    override fun disconnect() {
        marketScan.unBind()
    }

    override fun startScan(config: MarketScanConfig) {
        marketScan.onStartScan(config)
    }

    override fun cancelCurrentScan() {
        marketScan.cancelCurrentScan()
    }

}