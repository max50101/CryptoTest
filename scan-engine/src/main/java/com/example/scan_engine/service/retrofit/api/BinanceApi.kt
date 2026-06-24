package com.example.scan_engine.service.retrofit.api

import com.example.scan_engine.service.retrofit.model.CoinPriceDto
import retrofit2.http.GET
import retrofit2.http.Query


interface BinanceApi {
    @GET("api/v3/ticker/price")
    suspend fun getCoinBySymbol(@Query("symbol") symbol:String): CoinPriceDto
}