package com.example.data.coins.model

import com.google.gson.annotations.SerializedName

data class CoinsInfoDto(val symbols:List<BinanceSymbolDto>)

data class BinanceSymbolDto(
    val symbol:String,

    @SerializedName("baseAsset")
    val baseAsset:String,

    @SerializedName("quoteAsset")
    val quoteAsset:String,

    @SerializedName("isSpotTradingAllowed")
    val isSpotTradingAllowed: Boolean

)
