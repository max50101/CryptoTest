package com.example.data.coins.helpers

import com.example.database.entity.CoinsEntity
import com.example.model.Coin

fun CoinsEntity.toCoin(): Coin {
    return Coin(
        symbol = symbol,
        priceUsd = priceUsd,
        name = name,
        iconUrl = imageUrl
    )
}

fun Coin.toBinanceSymbol():String{
    return if(symbol.endsWith("USDT", ignoreCase = true)){
        symbol.uppercase()
    }else{
        "${symbol.uppercase()}USDT"
    }
}