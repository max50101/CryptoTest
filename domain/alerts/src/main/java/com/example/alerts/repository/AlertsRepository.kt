package com.example.alerts.repository

import com.example.model.Alert
import com.example.model.AlertStatus
import kotlinx.coroutines.flow.Flow

interface AlertsRepository {
    suspend fun getAlerts(): List<Alert>
    suspend fun insertAlert(alert: Alert)

    suspend fun getAlertsByStatus():List<Alert>
    fun observeAlertsByStatus(status: String): Flow<Alert>
    fun observeAlertsBySymbol(symbol: String): Flow<List<Alert>>
    suspend fun setStatus(id:Long, status: AlertStatus)
}