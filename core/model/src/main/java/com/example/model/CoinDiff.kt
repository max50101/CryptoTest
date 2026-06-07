package com.example.model

import androidx.recyclerview.widget.DiffUtil

object CoinsDiffCallback: DiffUtil.ItemCallback<Coin>(){
    override fun areItemsTheSame(
        oldItem: Coin,
        newItem: Coin
    ): Boolean {
        return oldItem.symbol==newItem.symbol
    }

    override fun areContentsTheSame(
        oldItem: Coin,
        newItem: Coin
    ): Boolean {
        return oldItem==newItem
    }

}