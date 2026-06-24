package com.example.market_scan.model

sealed interface ScannerConnectionState {

    data object Disconnected : ScannerConnectionState

    data object Connecting : ScannerConnectionState

    data object Connected : ScannerConnectionState

    data object NotInstalled : ScannerConnectionState

    data class Error(
        val message: String
    ) : ScannerConnectionState
}