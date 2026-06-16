package com.example.alert_worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.alert_worker.di.BootReceiverBackground
import com.example.alerts.usecases.StartAlertCheckUseCase
import javax.inject.Inject

class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var useCase: StartAlertCheckUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        (context!!.applicationContext as BootReceiverBackground).inject(this)
        if(intent!!.action==Intent.ACTION_BOOT_COMPLETED) {
            Log.i("BootReciever", "Application Started")
            useCase()
        }
    }
}