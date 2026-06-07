package com.example.coins.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.coins.R
import com.example.coins.databinding.CoinItemBinding
import com.example.model.Coin
import com.example.model.CoinsDiffCallback
import java.util.Locale
import java.util.Locale.getDefault

class CoinsAdapter: ListAdapter<Coin, CoinsAdapter.CoinViewHolder> (CoinsDiffCallback){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoinViewHolder {
        val view = CoinItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CoinViewHolder,
        position: Int
    ) {
        val coin=getItem(position)
        holder.bind(coin)
    }


    class CoinViewHolder(private val binding: CoinItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(coin: Coin){
            val coinSymbolLower = coin.baseAsset.lowercase()

            val iconUrl =
                "https://raw.githubusercontent.com/spothq/cryptocurrency-icons/master/128/color/$coinSymbolLower.png"
            if(iconUrl!=""){
                Glide.with(binding.root.context)
                    .load(iconUrl)
                    .placeholder(R.drawable.placeholder_coin)
                    .into(binding.coinImage)

            }else{
                binding.coinImage.setImageResource(R.drawable.placeholder_coin)
            }
            binding.coinSymbol.text=coin.symbol
            binding.coinName.text=coin.baseAsset
            binding.coinPrice.text=coin.priceUsd?.let { price->
                if(price>1) {
                    "$${"%.2f".format(price)}"
                }else{
                    "${price }"
                }
            }?: "-"
            binding.coin24hChange.text=coin.percentChange24h.toString()
        }

    }
}

