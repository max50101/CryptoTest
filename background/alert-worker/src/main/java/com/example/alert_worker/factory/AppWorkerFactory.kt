package com.example.alert_worker.factory

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.alert_worker.worker.AlertCheckWorker
import javax.inject.Inject

class AppWorkerFactory @Inject constructor(
    private val checkAlertCheckWorkerFactory: AlertCheckWorkerFactory
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            AlertCheckWorker::class.java.name -> {
                Log.d("CryptoAPP", "First fabric")
                checkAlertCheckWorkerFactory.create(appContext, workerParameters)
            }

            else -> null
        }
    }
}