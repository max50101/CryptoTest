package com.example.data.coins.api

import com.example.data.coins.model.CoinPriceDto
import com.example.data.coins.model.CoinsInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinsApi {
    @GET("api/v3/ticker/price")
    suspend fun getCoinBySymbol(@Query("symbol") symbol:String): CoinPriceDto


    @GET("api/v3/exchangeInfo")
    suspend fun getCoinsList(): CoinsInfoDto

}