package com.example.alerts.repository

interface AlertsSchedulerRepository {
    fun startAlertCheck()
    fun stopAlertCheck()
}