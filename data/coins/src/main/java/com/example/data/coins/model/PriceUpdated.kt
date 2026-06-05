package com.example.data.coins.model

data class PriceUpdated(val symbol:String, val price:Double)


data class BinanceTradeStreamDto(val stream:String,val data: BinanceTradeDto )

data class BinanceTradeDto(val s:String, val p: Double)