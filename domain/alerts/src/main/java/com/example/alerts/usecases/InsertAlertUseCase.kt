package com.example.alerts.usecases

import com.example.alerts.repository.AlertsRepository
import com.example.model.Alert
import javax.inject.Inject

class InsertAlertUseCase @Inject constructor(private val alertsRepository: AlertsRepository) {
        suspend operator fun invoke(alert: Alert){
            alertsRepository.insertAlert(alert)
        }
}