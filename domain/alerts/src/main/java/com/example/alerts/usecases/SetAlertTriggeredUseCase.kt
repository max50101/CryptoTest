package com.example.alerts.usecases

import com.example.alerts.repository.AlertsRepository
import com.example.model.Alert
import com.example.model.AlertStatus
import javax.inject.Inject

class SetAlertTriggeredUseCase @Inject constructor(private val alertsRepository: AlertsRepository) {
    suspend operator fun invoke(alert: Alert) {
        alertsRepository.setStatus(alert.id, AlertStatus.TRIGGERED)
    }
}