package com.example.scan_engine.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.scan_engine.service.retrofit.RetrofitClient
import com.example.scan_engine.service.search.CryptoSignalSearchEngine
import com.example.scanner_api.IMarketScanCallback
import com.example.scanner_api.IMarketScanService
import com.example.scanner_api.MarketScanOptionsDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong


class MarketScanService : Service() {

    private val taskIdGenerator = AtomicLong(0)
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val searchEngine = CryptoSignalSearchEngine(RetrofitClient.api)
    private val taskMap = ConcurrentHashMap<Long, Job>()
    private val binder = object : IMarketScanService.Stub() {
        override fun startScan(
            options: MarketScanOptionsDto?,
            callback: IMarketScanCallback?
        ): Long {
            val taskId = taskIdGenerator.incrementAndGet()
            val scanOptions = options ?: DEFAULT_OPTIONS
            callback.safeCall { onStarted(taskId) }

            val job = serviceScope.launch(start = CoroutineStart.LAZY) {
                runCatching {
                    val signals = searchEngine.search(
                        options = scanOptions,
                        onProgress = { processed, total, currentSymbol ->
                            callback.safeCall {
                                onProgress(taskId, processed, total, currentSymbol)
                            }
                        },
                        onSignalFound = { signal ->
                            callback.safeCall {
                                onSignalFound(taskId, signal)
                            }
                        }
                    )
                    callback.safeCall {
                        onCompleted(taskId, signals)
                    }
                }.onFailure { error ->
                    if (error is CancellationException) {
                        callback.safeCall { onCancelled(taskId) }
                    } else {
                        callback.safeCall {
                            onError(taskId, error.message ?: "Market scan failed")
                        }
                    }
                }
            }

            job.invokeOnCompletion {
                taskMap.remove(taskId)
            }
            taskMap[taskId] = job
            job.start()
            return taskId
        }

        override fun cancelScan(taskId: Long) {
            taskMap[taskId]?.cancel()
        }

        override fun isTaskRunning(taskId: Long): Boolean {
            return taskMap[taskId]?.isActive == true
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        taskMap.values.forEach(Job::cancel)
        taskMap.clear()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun IMarketScanCallback?.safeCall(block: IMarketScanCallback.() -> Unit) {
        if (this == null) return
        runCatching { block() }
    }

    private companion object {
        val DEFAULT_OPTIONS = MarketScanOptionsDto(
            quoteAsset = "USDT",
            interval = "15m",
            candleLimits = 100,
            maxSymbols = 100
        )
    }
}
