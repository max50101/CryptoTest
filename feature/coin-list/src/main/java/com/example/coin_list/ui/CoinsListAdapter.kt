package com.example.coin_list.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coin_list.R
import com.example.coin_list.databinding.CoinListItemBinding
import com.example.coin_list.databinding.FragmentCoinListBinding
import com.example.model.Coin
import com.example.model.CoinsDiffCallback

class CoinsListAdapter(private val onFavoriteClick: (coin: Coin) -> Unit, private val onCoinClick:(coin: Coin)->Unit) :
    ListAdapter<Coin, CoinsListAdapter.CoinListViewHolder>(CoinsDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoinListViewHolder {
        val binding = CoinListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinListViewHolder(binding, onFavoriteClick)
    }

    override fun onBindViewHolder(
        holder: CoinListViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position),{coin->onCoinClick(coin)})
    }


    class CoinListViewHolder(
        private val binding: CoinListItemBinding,
        val onFavoriteClick: (coin: Coin) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: Coin,onCoinClick: (coin: Coin) -> Unit) {
            binding.root.setOnClickListener { onCoinClick(coin) }
            val coinSymbolLower = coin.baseAsset.lowercase()
            val iconUrl =
                "https://raw.githubusercontent.com/spothq/cryptocurrency-icons/master/128/color/$coinSymbolLower.png"
            if (iconUrl != "") {
                Glide.with(binding.root.context)
                    .load(iconUrl)
                    .placeholder(R.drawable.placeholder_coin)
                    .into(binding.coinImage)

            } else {
                binding.coinImage.setImageResource(R.drawable.placeholder_coin)
            }
            binding.coinSymbol.text = coin.symbol
            binding.toggleFavorite.setImageResource(
                if (coin.isFavorite) {
                    R.drawable.ic_star_filled
                } else {
                    R.drawable.ic_star_outlined
                }
            )
            val colorRes = if (coin.isFavorite) {
                R.color.favorite_yellow
            } else {
                R.color.favorite_gray
            }
            binding.toggleFavorite.imageTintList= ColorStateList.valueOf(
                ContextCompat.getColor(binding.root.context,colorRes)
            )
            binding.toggleFavorite.setOnClickListener {
                onFavoriteClick(coin)
            }
        }
    }
}
