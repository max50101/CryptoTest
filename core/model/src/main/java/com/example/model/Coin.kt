package com.example.model

data class Coin(
    val symbol:String="",
    val baseAsset:String="",
    val quoteAsset:String="",
    val priceUsd:Double?=null,
    val percentChange24h:Double=0.0,
    val isFavorite: Boolean=false

)
