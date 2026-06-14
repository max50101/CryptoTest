package com.example.data.coins.model

import com.example.database.entity.CoinsEntity
import com.example.network.api.model.BinanceSymbolDto
import com.google.gson.annotations.SerializedName


fun BinanceSymbolDto.toCoinEntity(): CoinsEntity{
    return CoinsEntity(symbol,baseAsset,quoteAsset,null,status,false)
}
