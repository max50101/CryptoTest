package com.example.alerts.repository

import com.example.model.Alert

interface AlertNotificationSender {
    fun showAlertTriggered(alert: Alert, triggerPrice:Double)
}