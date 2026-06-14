package com.example.network.api.model


import com.google.gson.annotations.SerializedName

data class CoinsInfoDto(val symbols:List<BinanceSymbolDto>)

data class BinanceSymbolDto(
    val symbol:String,

    @SerializedName("baseAsset")
    val baseAsset:String,

    @SerializedName("quoteAsset")
    val quoteAsset:String,

    @SerializedName("status")
    val status:String,


)

