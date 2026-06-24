package com.example.scan_engine.service.retrofit.api

import com.example.scan_engine.service.retrofit.model.CoinPriceDto
import com.example.scan_engine.service.retrofit.model.Ticker24hDto
import com.google.gson.JsonArray
import retrofit2.http.GET
import retrofit2.http.Query


interface BinanceApi {
    @GET("api/v3/ticker/price")
    suspend fun getCoinBySymbol(@Query("symbol") symbol:String): CoinPriceDto

    @GET("api/v3/ticker/24hr")
    suspend fun get24hTickers(): List<Ticker24hDto>

    @GET("api/v3/klines")
    suspend fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int
    ): List<JsonArray>
}
