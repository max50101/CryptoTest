package com.example.scan_engine.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.scanner_api.IMarketScanCallback
import com.example.scanner_api.IMarketScanService
import com.example.scanner_api.MarketScanOptionsDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong


class MarketScanService(): Service() {

    private  val taskIdGenerator= AtomicLong(0)
    private val serviceScop= CoroutineScope(SupervisorJob()+ Dispatchers.Default)
    private val taskMap= ConcurrentHashMap<Long, Job>()
    private val binder=object : IMarketScanService.Stub(){
        override fun startScan(
            options: MarketScanOptionsDto?,
            callback: IMarketScanCallback?
        ): Long {
            val taskId=taskIdGenerator.incrementAndGet()
            callback?.onStarted(taskId)
            return taskId
        }

        override fun cancelScan(taskId: Long) {
            TODO("Not yet implemented")
        }

        override fun isTaskRunning(taskId: Long): Boolean {
            TODO("Not yet implemented")
        }

    }
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

}