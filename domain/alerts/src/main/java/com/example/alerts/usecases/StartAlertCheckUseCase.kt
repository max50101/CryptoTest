package com.example.alerts.usecases

import com.example.alerts.repository.AlertsSchedulerRepository
import javax.inject.Inject

class StartAlertCheckUseCase @Inject constructor(private val alertCheck: AlertsSchedulerRepository) {
     operator fun invoke(){
        alertCheck.startAlertCheck()
    }
}