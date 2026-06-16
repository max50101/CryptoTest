package com.example.alerts.usecases

import com.example.alerts.repository.AlertsSchedulerRepository
import javax.inject.Inject

class StopAlertCheckUseCase @Inject constructor(private val alertsSchedulerRepository: AlertsSchedulerRepository) {
    operator fun invoke(){
        alertsSchedulerRepository.stopAlertCheck()
    }
}