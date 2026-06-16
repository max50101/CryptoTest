package com.example.alerts.repository

interface MarketPriceRepository{
    suspend fun getCurrentPrices(
        symbols: Set<String>
    ): Map<String, Double>
}