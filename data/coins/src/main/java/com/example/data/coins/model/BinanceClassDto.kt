package com.example.data.coins.model

import com.example.model.CoinKline
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName


data class BinanceClassDto(
    @SerializedName("t")
    val openTimeMillis: Long,
    @SerializedName("s")
    val symbol: String,
    @SerializedName("o")
    val open: Double,
    @SerializedName("h")
    val high: Double,
    @SerializedName("l")
    val low: Double,
    @SerializedName("c")
    val close: Double,
    @SerializedName("v")
    val volume: Double,
    @SerializedName("T")
    val closeTimeMillis: Long,
    @SerializedName("q")
    val quoteAssetVolume: Double,
    @SerializedName("n")
    val numberOfTrades: Int,
    @SerializedName("V")
    val takerBuyBaseAssetVolume: Double,
    @SerializedName("Q")
    val takeBuyQuoteAssetVolume: Double

)

data class BinanceStreamKline(val s: String, val data: BinanceStreamKlineData)

data class BinanceStreamKlineData(val k: BinanceClassDto)
fun List<JsonElement>.toBinanceKlineDto(symbol: String): BinanceClassDto {
    return BinanceClassDto(
        openTimeMillis = this[0].asLong,
        symbol = symbol,
        open = this[1].asDouble,
        high = this[2].asDouble,
        low = this[3].asDouble,
        close = this[4].asDouble,
        volume = this[5].asDouble,
        closeTimeMillis = this[6].asLong,
        quoteAssetVolume = this[7].asDouble,
        numberOfTrades = this[8].asInt,
        takerBuyBaseAssetVolume = this[9].asDouble,
        takeBuyQuoteAssetVolume = this[10].asDouble
    )
}

fun BinanceClassDto.toCoinKline(interval: String): CoinKline {
    return CoinKline(
        symbol,
        interval,
        open,
        close,
        high,
        low,
        System.currentTimeMillis() >= closeTimeMillis,
        openTimeMillis,
        closeTimeMillis
    )
}