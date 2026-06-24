package com.example.data.coins.api.websocket

import android.util.Log
import com.example.data.coins.model.BinanceClassDto
import com.example.data.coins.model.BinanceStreamKline
import com.example.data.coins.model.BinanceTradeStreamDto
import com.example.data.coins.model.PriceUpdated
import com.example.network.NetworkSettings
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.retryWhen
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.net.UnknownHostException
import java.util.Locale
import java.util.Locale.getDefault
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

inline fun <reified T> Gson.fromJsonTyped(json:String):T{
    return fromJson(json,T::class.java)
}

class BinanceWebSocket @Inject constructor(val okHttp: OkHttpClient) {
    private val gson= Gson()

    fun observePrices(coins:List<String>):Flow<PriceUpdated>{
        return createWebSocketFlow(coins).retryWhen { cause, attempt ->
            if(cause is UnknownHostException){
                false
            }else {
                val delayMs = when {
                    attempt < 3 -> 1_000L
                    attempt < 5 -> 3_000L
                    else -> 10_000L
                }
                delay(delayMs.milliseconds)
                true
            }
        }
    }

    fun observerCoinDetails(coin:String, interval: String):Flow<BinanceClassDto>{
        return createWebSocketFlowDetails(coin,interval).retryWhen { cause, attempt ->
            if(cause is UnknownHostException){
                false
            }else{
                val delayMs=when{
                    attempt<3->1_000L
                    attempt<5->3_000L
                    else->10_000L
                }
                delay(delayMs.milliseconds)
                true
            }
        }
    }
    private fun createWebSocketFlowDetails(coin:String,interval:String):Flow<BinanceClassDto> =callbackFlow {
        val request= Request.Builder().url(buildDetailsUrl(coin,interval)).build()
        val webSocket=okHttp.newWebSocket(
            request,
            object : WebSocketListener() {

                override fun onMessage(webSocket: WebSocket, text: String) {

                    val kline: BinanceClassDto? = parseBinanceKline(text)
                    if(kline!=null){
                        trySend(kline)
                    }
                }
                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e("WebSocker","${t.message}")
                    close(t)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    close()
                }
            }
        )
        awaitClose {
            webSocket.close(1000,"flow canceled")
        }
    }
    private fun createWebSocketFlow(coins:List<String>): Flow<PriceUpdated> = callbackFlow{
        if(coins.isEmpty()){
            close()
            return@callbackFlow
        }
        val request= Request.Builder().url(buildUrl(coins)).build()
        val webSocker=okHttp.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text:String) {

                    val update: PriceUpdated? = parsePriceUpdate(text);
                    if(update!=null){
                        trySend(update)
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    close(t)
                }
                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    close()
                }

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)

                }
            }
        )
        awaitClose {
            webSocker.close(1000,"flow canceled")
        }

    }

    private fun buildUrl(coins:List<String>):String{
        return NetworkSettings.BASE_SOCKET_URL+"/stream?streams="+coins.map(String::lowercase).map{s->s+"@trade"}.joinToString("/")
    }

    private fun buildDetailsUrl(coin:String, interval:String):String{
        return NetworkSettings.BASE_SOCKET_URL+"/stream?streams=${coin.lowercase(getDefault())}@kline_${interval}"
    }

    private fun parsePriceUpdate(text:String): PriceUpdated?{
        return runCatching {
            val dto: BinanceTradeStreamDto=gson.fromJsonTyped(text)
            PriceUpdated(
                dto.data.s,dto.data.p
            )
        }.getOrNull()
    }

    private fun parseBinanceKline(text:String): BinanceClassDto?{
        return runCatching {
            val dto: BinanceStreamKline=gson.fromJsonTyped(text)
            dto.data.k
        }.getOrNull()
    }


}