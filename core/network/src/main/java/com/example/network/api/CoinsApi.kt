package com.example.network.api


import com.example.network.api.model.CoinPriceDto
import com.example.network.api.model.CoinsInfoDto
import com.google.gson.JsonElement
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinsApi {
    @GET("api/v3/ticker/price")
    suspend fun getCoinBySymbol(@Query("symbol") symbol:String): CoinPriceDto

    @GET("api/v3/ticker/price")
    suspend fun getCoinsPriceBySymbol(@Query("symbols")  symbols:List<String>):List<CoinPriceDto>

    @GET("api/v3/ticker/price")
    suspend fun getCoinsPriceBySymbol(@Query("symbols")  symbols: String):List<CoinPriceDto>

    @GET("api/v3/exchangeInfo")
    suspend fun getCoinDetails(@Query("symbol") symbol: String): CoinsInfoDto

    @GET("api/v3/exchangeInfo")
    suspend fun getCoinsList(): CoinsInfoDto

    @GET("api/v3/uiKlines")
    suspend fun getUiKlines(@Query("symbol") symbol: String, @Query("interval") interval:String):List<List<JsonElement>>

}