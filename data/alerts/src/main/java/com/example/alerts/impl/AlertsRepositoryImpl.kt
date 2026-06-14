package com.example.alerts.impl

import com.example.alerts.mapper.toAlert
import com.example.alerts.mapper.toAlertEntity
import com.example.alerts.mapper.toEntity
import com.example.alerts.repository.AlertsRepository
import com.example.database.entity.AlertStatusEntity
import com.example.database.features.AlertsDao
import com.example.model.Alert
import com.example.model.AlertStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlertsRepositoryImpl @Inject constructor(private val alertsDao: AlertsDao) :
    AlertsRepository {
    override suspend fun getAlerts(): List<Alert> {
        return alertsDao.getAlertsByStatus(AlertStatusEntity.ACTIVE).map { it.toAlert() }
    }

    override suspend fun insertAlert(alert: Alert) {
        alertsDao.insertAlert(alert.toAlertEntity())
    }

    override fun observeAlertsByStatus(status: String): Flow<Alert> {
        TODO("Not yet implemented")
    }

    override fun observeAlertsBySymbol(symbol: String): Flow<List<Alert>> {
        return alertsDao.observeAlertsBySymbol(symbol, AlertStatus.ACTIVE.toEntity())
            .map { it -> it.map { alert -> alert.toAlert() } }
    }

    override suspend fun setStatus(
        id: Long,
        status: AlertStatus
    ): Flow<Alert> {
        TODO("Not yet implemented")
    }
}