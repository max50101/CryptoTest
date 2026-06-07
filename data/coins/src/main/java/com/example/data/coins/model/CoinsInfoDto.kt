package com.example.data.coins.model

import com.example.database.entity.CoinsEntity
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


fun BinanceSymbolDto.toCoinEntity(): CoinsEntity{
    return CoinsEntity(symbol,baseAsset,quoteAsset,null,status,false)
}
