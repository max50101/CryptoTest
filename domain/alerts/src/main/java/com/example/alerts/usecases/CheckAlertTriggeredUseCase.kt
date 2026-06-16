package com.example.alerts.usecases

import android.util.Log
import com.example.alerts.repository.AlertNotificationSender
import com.example.alerts.repository.AlertsRepository
import com.example.alerts.repository.MarketPriceRepository
import com.example.model.Alert
import com.example.model.AlertStatus
import com.example.model.ConditionsType
import javax.inject.Inject

class CheckAlertTriggeredUseCase @Inject constructor(
    private val alertsRepository: AlertsRepository,
    private val marketPriceRepository: MarketPriceRepository,
    private val alertNotificationSender: AlertNotificationSender
) {
    suspend operator fun invoke() {
        var activeAlerts = alertsRepository.getAlertsByStatus()
        val symbols = activeAlerts.map { it.symbol }.toSet()
        val currentPrices = marketPriceRepository.getCurrentPrices(symbols)
        Log.i("Working on CheckAlertTriggeredUseCase", "${currentPrices}")
        activeAlerts=activeAlerts.filter { alert ->
            val price = currentPrices[alert.symbol] ?: return@filter false
            when (alert.conditionType) {
                ConditionsType.ABOVE -> price > alert.targetPrice
                ConditionsType.BELLOW -> price < alert.targetPrice
            }
        }
        activeAlerts.forEach {
            alertsRepository.setStatus(it.id, AlertStatus.TRIGGERED)
            alertNotificationSender.showAlertTriggered(it, currentPrices[it.symbol] ?:0.0)
        }

    }
}