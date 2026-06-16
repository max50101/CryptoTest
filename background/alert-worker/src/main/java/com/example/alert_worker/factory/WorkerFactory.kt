package com.example.alert_worker.factory

import android.content.Context
import android.util.Log
import androidx.work.WorkerParameters
import com.example.alert_worker.worker.AlertCheckWorker
import com.example.alerts.usecases.CheckAlertTriggeredUseCase
import javax.inject.Inject

class AlertCheckWorkerFactory @Inject constructor(private val checkAlertTriggeredUseCase: CheckAlertTriggeredUseCase) {
    fun create(
        context: Context,
        workerParameters: WorkerParameters
    ): AlertCheckWorker{
        Log.d("CryptoAPP", "Second Fabric")
        return AlertCheckWorker(context,workerParameters,checkAlertTriggeredUseCase)
    }
}