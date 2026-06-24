package com.example.market_scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market_scan.model.MarketScanConfig
import com.example.market_scan.model.MarketScanState
import com.example.market_scan.model.ScannerConnectionState
import com.example.market_scan.repository.MarketScanRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MarketScanViewModel(private val marketScanRepo: MarketScanRepository) : ViewModel() {
    val scanState: StateFlow<MarketScanState> = marketScanRepo.scanState().stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000L), MarketScanState.Idle
    )
    val connectionState: StateFlow<ScannerConnectionState> =
        marketScanRepo.connectionState().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L), ScannerConnectionState.Disconnected
        )

    fun onScreenStarted() {
        marketScanRepo.connect()
    }

    fun disconnect() {
        val currentScanState=scanState
        marketScanRepo.disconnect()

    }
    fun startScan() {
        marketScanRepo.startScan(MarketScanConfig())
    }

    fun cancelScan() {
        marketScanRepo.cancelCurrentScan()

    }

    override fun onCleared() {
        marketScanRepo.disconnect()
        super.onCleared()
    }

}