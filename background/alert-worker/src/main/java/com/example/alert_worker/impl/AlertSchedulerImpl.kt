package com.example.alert_worker.impl

import android.app.Application

import androidx.constraintlayout.widget.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.alert_worker.worker.AlertCheckWorker
import com.example.alerts.repository.AlertsSchedulerRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AlertSchedulerImpl @Inject constructor(private val application: Application) :
    AlertsSchedulerRepository {
    override fun startAlertCheck() {
        val constraints =
            androidx.work.Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val checkAlertWorkRequest =
            PeriodicWorkRequestBuilder<AlertCheckWorker>(15, TimeUnit.MINUTES).setConstraints(
                constraints
            )
                .build()
        WorkManager.getInstance(application).enqueueUniquePeriodicWork(
            UNIQUE_ALERT_SCHEDULER,
            ExistingPeriodicWorkPolicy.REPLACE, checkAlertWorkRequest)

    }

    override fun stopAlertCheck() {
        WorkManager.getInstance(application).cancelUniqueWork(UNIQUE_ALERT_SCHEDULER)
    }

    companion object {
        const val UNIQUE_ALERT_SCHEDULER = "ALERT_SCHDULER"
    }

}