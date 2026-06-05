package com.example.model

data class Coin(
    val symbol:String="",
    val name:String="",
    val priceUsd:Double?,
    val percentChange24h:Double=0.0,
    val iconUrl:String?
)
