package com.example.feature.live_notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.domain.coins.usecases.ObserveCoinPriceUseCase
import com.example.live_notification.di.NotificationInjector
import com.example.live_notification.usecases.ClearNotificationSymbolUseCase
import com.example.live_notification.usecases.SetCurrentNotificationSymbolUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale.getDefault
import javax.inject.Inject


class NotificationForegroundService : Service() {

    @Inject
    lateinit var observeCoinDetailsUseCase: ObserveCoinPriceUseCase

    @Inject
    lateinit var setCurrentNotificationSymbolUseCase: SetCurrentNotificationSymbolUseCase

    @Inject
    lateinit var clearNotificationSymbolUseCase: ClearNotificationSymbolUseCase
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var cachedIcon: Bitmap? = null
    private var cachedIconSymbol: String? = null
    private var observeJob: Job? = null
    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        (application as NotificationInjector)
            .inject(this)
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!hasNotificationPermission()) {
            stopSelf()
            return START_NOT_STICKY
        } else {
            val symbolNullable = intent?.getStringExtra(EXTRA_SYMBOL)
            if (symbolNullable == null) {
                stopSelf()
                return START_NOT_STICKY
            } else {

                val notification = createNotification(symbolNullable, "")
                ServiceCompat.startForeground(
                    this, NOTIFICATION_ID, notification,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC else 0
                )
                cachedIconSymbol = symbolNullable
                observeJob?.cancel()
                observeJob = scope.launch {
                    setCurrentNotificationSymbolUseCase(symbolNullable)
                    observeCoinDetailsUseCase(symbolNullable).catch { throwable ->
                        Toast.makeText(
                            applicationContext, throwable.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                        .collect { coin ->
                            if (cachedIcon == null && cachedIconSymbol == coin.baseAsset) {
                                loadIconAndRefresh(coin.baseAsset)
                            }
                            updateNotification(coin.symbol, coin.priceUsd.toString())
                        }
                }

            }
            return START_STICKY
        }
    }

    private fun createNotification(symbol: String, priceText: String): Notification {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_coin_image_placeholder)
            .setContentTitle(symbol)
            .setContentText("Price: $priceText")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        cachedIcon?.let {
            notification.setLargeIcon(it)
        }
        return notification.build()

    }

    suspend private fun loadIconAndRefresh(symbol: String) {
        try {
            val iconUrl =
                "https://raw.githubusercontent.com/spothq/cryptocurrency-icons/master/128/color/${
                    symbol.lowercase(
                        getDefault()
                    )
                }.png"
            val bitmap = Glide.with(applicationContext)
                .asBitmap()
                .load(iconUrl)
                .submit()
                .get()
            cachedIcon = bitmap
            cachedIconSymbol = symbol
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateNotification(symbol: String, priceText: String) {
        if (hasNotificationPermission()) {
            val notification = createNotification(symbol, priceText)
            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification)
        }
    }


    override fun onDestroy() {
        runBlocking {
            clearNotificationSymbolUseCase()
        }
        scope.cancel()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID, "Live channel", importance
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }



    companion object {
        const val EXTRA_SYMBOL = "symbol"

        private const val CHANNEL_ID = "price_channel"
        private const val NOTIFICATION_ID = 1001
    }


    override fun onBind(intent: Intent?): IBinder? = null
}