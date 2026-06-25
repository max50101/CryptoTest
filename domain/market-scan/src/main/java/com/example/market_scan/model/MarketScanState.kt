package com.example.market_scan.model

sealed interface MarketScanState {

    data object Idle : MarketScanState


    data class Running(
        val taskId: Long,
        val processed: Int,
        val total: Int,
        val currentSymbol: String?,
        val signals: List<MarketSignal>
    ) : MarketScanState {

        val progressPercent: Int
            get() {
                if (total <= 0) return 0
                return ((processed.toDouble() / total.toDouble()) * 100).toInt()
            }
    }

    data class Completed(
        val taskId: Long,
        val signals: List<MarketSignal>
    ) : MarketScanState

    data class Error(
        val taskId: Long?,
        val message: String
    ) : MarketScanState

    data class Cancelled(
        val taskId: Long
    ) : MarketScanState
}

data class MarketSignal(
    val symbol: String,
    val type: SignalType,
    val description: String,
    val score: Int,
    val priceChangePercent: Double,
    val volumeSpikeMultiplier: Double
)


enum class SignalType {
    VolumeSpike,
    PricePump,
    PriceDump,
    Breakout,
    HighVolatility,
    RSI,
    UNKNOWN,
}