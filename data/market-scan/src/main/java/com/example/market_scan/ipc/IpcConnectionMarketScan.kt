package com.example.market_scan.ipc

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.market_scan.mappers.toDto
import com.example.market_scan.model.MarketScanConfig
import com.example.market_scan.model.MarketScanState
import com.example.market_scan.model.ScannerConnectionState
import com.example.scanner_api.IMarketScanCallback
import com.example.scanner_api.IMarketScanService
import com.example.scanner_api.MarketSignalDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class IpcConnectionMarketScan @Inject constructor(private val application: Application) {
    private val context=application.applicationContext
    private var service: IMarketScanService?=null
    private var isBound: Boolean=false

    private var currentTaskId: Long? = null
    private val _marketScanState= MutableStateFlow<MarketScanState>(MarketScanState.Idle)
    val marketScanState=_marketScanState.asStateFlow()

    private val _connectionState= MutableStateFlow<ScannerConnectionState>(ScannerConnectionState.Disconnected)
    val connectionState=_connectionState.asStateFlow()

    private val connection= object: ServiceConnection{
        override fun onServiceConnected(
            name: ComponentName?,
            binder: IBinder?
        ) {
            service= IMarketScanService.Stub.asInterface(binder)
            isBound=true
            _connectionState.value= ScannerConnectionState.Connected
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            isBound = false
            _connectionState.value = ScannerConnectionState.Disconnected
        }

        override fun onBindingDied(name: ComponentName) {
            service = null
            isBound = false
            _connectionState.value = ScannerConnectionState.Error(
                message = "Scanner service binding died"
            )
        }

        override fun onNullBinding(name: ComponentName) {
            service = null
            isBound = false
            _connectionState.value = ScannerConnectionState.Error(
                message = "Scanner service returned null binding"
            )
        }
    }

    private val connectionCallback: IMarketScanCallback= object : IMarketScanCallback.Stub() {
        override fun onStarted(taskId: Long) {
            Log.i("Connection callback","Started")
        }

        override fun onProgress(taskId: Long, processed: Int, total: Int, currentSymbol: String?) {
            Log.i("Connection callback",currentSymbol?:"empty")
        }

        override fun onSignalFound(taskId: Long, signal: MarketSignalDto?) {
            Log.i("Connection callback",signal.toString()?:"emty")
        }

        override fun onCompleted(taskId: Long, signals: List<MarketSignalDto?>?) {
            Log.i("Connection callback","onCompleted")
        }

        override fun onError(taskId: Long, message: String?) {
            Log.i("Connection callback","onError")
        }

        override fun onCancelled(taskId: Long) {
            Log.i("ConnectionCallback", "onCancelled taskId=$taskId")
        }


    }

    fun bind(){
        if(isBound) return

        _connectionState.value= ScannerConnectionState.Connecting
        val intent= Intent("com.example.scanner_api.MARKET_SCAN_SERVICE").apply {
            setPackage("com.example.scan_engine")
        }

        val bound=context.bindService(intent,connection,Context.BIND_AUTO_CREATE)
        if(!bound){
            _connectionState.value= ScannerConnectionState.NotInstalled
        }
    }

    fun unBind(){
        if (!isBound) return
        runCatching {
            context.unbindService(connection)
        }
        service=null
        isBound=false
        _connectionState.value= ScannerConnectionState.Disconnected
    }

    fun onStartScan(config: MarketScanConfig){
        val serviceScan=service
        if(serviceScan==null){
            _marketScanState.value= MarketScanState.Error(null,"Scanner service is not connected")
            return
        }
        runCatching {
            val taskId=serviceScan.startScan(config.toDto(),connectionCallback)
            currentTaskId=taskId
        }.onFailure { error->
            _marketScanState.value= MarketScanState.Error(null,error.message?:"Failed to start market scan")
        }

    }

    fun cancelCurrentScan() {
        val taskId = currentTaskId ?: return
        val remoteService = service ?: return

        runCatching {
            remoteService.cancelScan(taskId)
        }.onFailure { error ->
            _marketScanState.value = MarketScanState.Error(
                taskId = taskId,
                message = error.message ?: "Failed to cancel scan"
            )
        }
    }

    


}