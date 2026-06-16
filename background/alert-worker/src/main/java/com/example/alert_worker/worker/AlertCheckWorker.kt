package com.example.alert_worker.worker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.alerts.usecases.CheckAlertTriggeredUseCase
import java.io.IOException

class AlertCheckWorker(
    appContext: Context,
    params: WorkerParameters,
    private val checkAlertTriggeredUseCase: CheckAlertTriggeredUseCase
): CoroutineWorker(appContext,params) {

    override suspend fun doWork(): Result {
        return try{
            checkAlertTriggeredUseCase()
            Result.success()
        }catch (e: IOException){
            Log.e("Worker",e.message!!)
            Result.retry()
        }catch (e: Exception){
            Log.e("Worker",e.message!!)
            Result.failure()
        }
    }
}