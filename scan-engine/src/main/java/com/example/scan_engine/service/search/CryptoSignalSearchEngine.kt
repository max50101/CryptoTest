package com.example.scan_engine.service.search

import com.example.scan_engine.service.retrofit.api.BinanceApi
import com.example.scan_engine.service.retrofit.model.Ticker24hDto
import com.example.scanner_api.MarketScanOptionsDto
import com.example.scanner_api.MarketSignalDto
import com.google.gson.JsonArray
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.util.Locale
import kotlin.coroutines.coroutineContext
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CryptoSignalSearchEngine(
    private val api: BinanceApi
) {
    suspend fun search(
        options: MarketScanOptionsDto,
        onProgress: suspend (processed: Int, total: Int, currentSymbol: String) -> Unit,
        onSignalFound: suspend (MarketSignalDto) -> Unit
    ): List<MarketSignalDto> {
        val quoteAsset = options.quoteAsset.ifBlank { DEFAULT_QUOTE_ASSET }.uppercase()
        val interval = options.interval.ifBlank { DEFAULT_INTERVAL }
        val candleLimit = options.candleLimits.coerceIn(MIN_CANDLE_LIMIT, MAX_CANDLE_LIMIT)
        val maxSymbols = max(options.maxSymbols, 1)

        val tickers = api.get24hTickers()
            .asSequence()
            .filter { it.symbol.endsWith(quoteAsset) }
            .sortedByDescending { it.quoteVolume.toDoubleOrNull() ?: 0.0 }
            .take(maxSymbols)
            .toList()

        val signals = mutableListOf<MarketSignalDto>()
        tickers.forEachIndexed { index, ticker ->
            currentCoroutineContext().ensureActive()
            onProgress(index + 1, tickers.size, ticker.symbol)

            buildPriceChangeSignal(ticker)?.also { signal ->
                signals += signal
                onSignalFound(signal)
            }

            val candles = getCandles(ticker.symbol, interval, candleLimit)

            buildRsiSignal(ticker.symbol, candles)?.also { signal ->
                signals += signal
                onSignalFound(signal)
            }

            buildVolumeSignal(ticker.symbol, candles)?.also { signal ->
                signals += signal
                onSignalFound(signal)
            }
        }

        return signals
    }

    private fun buildPriceChangeSignal(ticker: Ticker24hDto): MarketSignalDto? {
        val percent = ticker.priceChangePercent.toDoubleOrNull() ?: return null
        if (abs(percent) < PRICE_CHANGE_THRESHOLD_PERCENT) return null

        val direction = if (percent >= 0.0) "up" else "down"
        return MarketSignalDto(
            symbol = ticker.symbol,
            type = TYPE_PRICE_CHANGE_24H,
            description = "24h price moved $direction by ${percent.formatPercent()}%",
            score = min(100, (abs(percent) * PRICE_SCORE_MULTIPLIER).toInt()),
            priceChangePercent = percent,
            volumeSpikeMultiplier = 0.0
        )
    }

    private suspend fun getCandles(symbol: String, interval: String, limit: Int): List<Candle> {
        return try {
            api.getKlines(symbol, interval, limit).mapNotNull{it-> it.toCandle() }
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun buildRsiSignal(symbol: String, candles: List<Candle>): MarketSignalDto? {
        if (candles.size < RSI_PERIOD + 2) return null

        val closes = candles.map(Candle::closePrice)
        val previousRsi = calculateRsi(closes.dropLast(1), RSI_PERIOD) ?: return null
        val latestRsi = calculateRsi(closes, RSI_PERIOD) ?: return null
        val rsiDelta = latestRsi - previousRsi

        val isSpike = latestRsi >= RSI_OVERBOUGHT_THRESHOLD && previousRsi < RSI_OVERBOUGHT_THRESHOLD ||
            rsiDelta >= RSI_SPIKE_DELTA && latestRsi >= RSI_SPIKE_MIN_VALUE
        if (!isSpike) return null

        return MarketSignalDto(
            symbol = symbol,
            type = TYPE_RSI_SPIKE,
            description = "RSI spiked from ${previousRsi.formatPercent()} to ${latestRsi.formatPercent()}",
            score = min(100, (latestRsi + max(rsiDelta, 0.0)).toInt()),
            priceChangePercent = 0.0,
            volumeSpikeMultiplier = 0.0
        )
    }

    private fun buildVolumeSignal(symbol: String, candles: List<Candle>): MarketSignalDto? {
        if (candles.size < MIN_VOLUME_CANDLES) return null

        val latestVolume = candles.last().volume
        val averageVolume = candles
            .dropLast(1)
            .takeLast(VOLUME_AVERAGE_PERIOD)
            .map(Candle::volume)
            .average()
        if (averageVolume <= 0.0) return null

        val multiplier = latestVolume / averageVolume
        if (multiplier < VOLUME_SPIKE_MULTIPLIER) return null

        return MarketSignalDto(
            symbol = symbol,
            type = TYPE_VOLUME_SPIKE,
            description = "Volume is ${multiplier.formatMultiplier()}x above recent average",
            score = min(100, (multiplier * VOLUME_SCORE_MULTIPLIER).toInt()),
            priceChangePercent = 0.0,
            volumeSpikeMultiplier = multiplier
        )
    }

    private fun calculateRsi(closes: List<Double>, period: Int): Double? {
        if (closes.size <= period) return null

        val recentCloses = closes.takeLast(period + 1)
        var gains = 0.0
        var losses = 0.0

        for (index in 1 until recentCloses.size) {
            val change = recentCloses[index] - recentCloses[index - 1]
            if (change >= 0.0) {
                gains += change
            } else {
                losses += abs(change)
            }
        }

        val averageGain = gains / period
        val averageLoss = losses / period
        if (averageLoss == 0.0) return 100.0

        val relativeStrength = averageGain / averageLoss
        return 100.0 - (100.0 / (1.0 + relativeStrength))
    }

    private fun JsonArray.toCandle(): Candle? {
        val closePrice = getOrNull(KLINE_CLOSE_INDEX)?.asString?.toDoubleOrNull() ?: return null
        val volume = getOrNull(KLINE_VOLUME_INDEX)?.asString?.toDoubleOrNull() ?: return null
        return Candle(closePrice = closePrice, volume = volume)
    }

    private fun JsonArray.getOrNull(index: Int) = if (index in 0 until size()) get(index) else null

    private fun Double.formatPercent(): String = String.format(Locale.US, "%.2f", this)

    private fun Double.formatMultiplier(): String = String.format(Locale.US, "%.2f", this)

    private companion object {
        const val DEFAULT_QUOTE_ASSET = "USDT"
        const val DEFAULT_INTERVAL = "15m"
        const val MIN_CANDLE_LIMIT = 30
        const val MAX_CANDLE_LIMIT = 500
        const val PRICE_CHANGE_THRESHOLD_PERCENT = 3.0
        const val PRICE_SCORE_MULTIPLIER = 10
        const val RSI_PERIOD = 14
        const val RSI_OVERBOUGHT_THRESHOLD = 70.0
        const val RSI_SPIKE_DELTA = 10.0
        const val RSI_SPIKE_MIN_VALUE = 55.0
        const val VOLUME_AVERAGE_PERIOD = 20
        const val MIN_VOLUME_CANDLES = VOLUME_AVERAGE_PERIOD + 1
        const val VOLUME_SPIKE_MULTIPLIER = 2.0
        const val VOLUME_SCORE_MULTIPLIER = 25
        const val KLINE_CLOSE_INDEX = 4
        const val KLINE_VOLUME_INDEX = 5
        const val TYPE_PRICE_CHANGE_24H = "PRICE_CHANGE_24H"
        const val TYPE_RSI_SPIKE = "RSI_SPIKE"
        const val TYPE_VOLUME_SPIKE = "VOLUME_SPIKE"
    }
}
