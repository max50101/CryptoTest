package com.example.market_scan.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.feature.market_scan.R
import com.example.feature.market_scan.databinding.ItemMarketSignalBinding
import com.example.market_scan.model.MarketSignal
import com.example.market_scan.model.SignalType
import java.util.Locale
import kotlin.math.abs

class MarketSignalsAdapter :
    ListAdapter<MarketSignal, MarketSignalsAdapter.SignalViewHolder>(MarketSignalDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignalViewHolder {
        val binding = ItemMarketSignalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SignalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SignalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SignalViewHolder(
        private val binding: ItemMarketSignalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(signal: MarketSignal) {
            binding.symbolText.text = signal.symbol
            binding.typeText.text = signal.type.displayName()
            binding.descriptionText.text = signal.description
            binding.scoreText.text = signal.score.toString()
            binding.metricsText.text = signal.metricsText()

            val context = binding.root.context
            val scoreColor = when {
                signal.priceChangePercent < 0.0 -> R.color.market_scan_negative
                signal.score >= 70 -> R.color.market_scan_positive
                else -> R.color.market_scan_text_primary
            }
            binding.scoreText.setTextColor(ContextCompat.getColor(context, scoreColor))
        }

        private fun MarketSignal.metricsText(): String {
            val priceText = if (priceChangePercent == 0.0) {
                "0.00%"
            } else {
                val sign = if (priceChangePercent > 0.0) "+" else "-"
                "$sign${String.format(Locale.US, "%.2f", abs(priceChangePercent))}%"
            }
            val volumeText = if (volumeSpikeMultiplier > 0.0) {
                "${String.format(Locale.US, "%.2f", volumeSpikeMultiplier)}x volume"
            } else {
                "volume n/a"
            }
            return "$priceText / $volumeText"
        }

        private fun SignalType.displayName(): String {
            return when (this) {
                SignalType.VolumeSpike -> "Volume spike"
                SignalType.PricePump -> "Price pump"
                SignalType.PriceDump -> "Price dump"
                SignalType.Breakout -> "Breakout"
                SignalType.HighVolatility -> "High volatility"
                SignalType.RSI -> "RSI spike"
                SignalType.UNKNOWN -> "Signal"
            }
        }
    }
}

private object MarketSignalDiffCallback : DiffUtil.ItemCallback<MarketSignal>() {
    override fun areItemsTheSame(oldItem: MarketSignal, newItem: MarketSignal): Boolean {
        return oldItem.symbol == newItem.symbol &&
            oldItem.type == newItem.type &&
            oldItem.description == newItem.description
    }

    override fun areContentsTheSame(oldItem: MarketSignal, newItem: MarketSignal): Boolean {
        return oldItem == newItem
    }
}
