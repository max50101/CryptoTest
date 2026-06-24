package com.example.market_scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.market_scan.repository.MarketScanRepository
import javax.inject.Inject

class MarketScanViewModelFactory @Inject constructor(private val marketScanRepository: MarketScanRepository):
    ViewModelProvider.Factory {


        @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MarketScanViewModel(marketScanRepo = marketScanRepository ) as T
    }
}