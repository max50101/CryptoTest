package com.example.alert_worker.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alerts.R
import com.example.alerts.repository.AlertNotificationSender
import com.example.model.Alert
import javax.inject.Inject

class AlertNotificationSenderImpl @Inject constructor(private val application: Application) :
    AlertNotificationSender {
    @SuppressLint("MissingPermission")
    override fun showAlertTriggered(alert: Alert, triggerPrice: Double) {
        if(!canSendNotification()){
            return
        }
        val notification = createNotification(alert, triggerPrice)
        NotificationManagerCompat.from(application).notify(alert.id.toInt(),notification)
    }

    init {
        createNotificationChannel()
    }

    private fun canSendNotification(): Boolean {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    private fun createNotification(alert: Alert, triggerPrice: Double): Notification {
        return NotificationCompat
            .Builder(application, CHANNEL_ID)
            .setSmallIcon(com.example.alert_worker.R.drawable.ic_small_icon)
            .setContentTitle("Alert triggered for ${alert.symbol}")
            .setContentText("Current price ${triggerPrice} , target Price : ${alert.targetPrice}")
            .setOngoing(false)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, "Alert channel", NotificationManager.IMPORTANCE_LOW)
            val notificationManager: NotificationManager = application.getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val EXTRA_ALERT = "alert"

        private const val CHANNEL_ID = "alert_channel"
    }

}