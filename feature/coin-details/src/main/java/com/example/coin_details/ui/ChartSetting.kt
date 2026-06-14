package com.example.coin_details.ui


import com.example.model.CoinKline
import com.patrykandpatrick.vico.views.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.views.cartesian.axis.Axis
import com.patrykandpatrick.vico.views.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.views.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.views.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.views.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.views.common.data.ExtraStore
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.min
import kotlin.math.pow

fun calculateYStep(minY: Double, maxY: Double): Double {
    val range = maxY - minY
    return when {
        range <= 0.001 -> 0.0001
        range <= 0.005 -> 0.0005
        range <= 0.01 -> 0.0005
        range <= 0.05 -> 0.001
        range <= 0.1 -> 0.005
        range <= 0.5 -> 0.025
        range <= 1.0 -> 0.05
        range <= 5.0 -> 0.25
        range <= 10.0 -> 0.5
        range <= 50.0 -> 2.5
        range <= 100.0 -> 5.0
        range <= 500.0 -> 25.0
        range <= 1000.0 -> 50.0
        range <= 5000.0 -> 250.0
        else -> 500.0
    }
}

const val MS_IN_H = 3.6e+6
const val MS_IN_1D = (3.6e+6) * 24
const val MS_IN_4H = (3.6e+6) * 4
const val MS_IN_15M = (3.6e+6) / 4
const val MS_IN_30M = (3.6e+6) / 2

val RangeProvider =
    object : CartesianLayerRangeProvider {

        override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
            val yStep = calculateYStep(minY, maxY)
            return yStep * floor(minY / yStep)
        }

        override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
            val yStep = calculateYStep(minY, maxY)
            return yStep * ceil(maxY / yStep)
        }
    }

val StartAxisValueFormatter = CartesianValueFormatter.decimal(DecimalFormat("$#,###"))

val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ 20.0 })

fun createBottomAxisValueFormatter(
    interval: String,
    klines: List<CoinKline>
): CartesianValueFormatter {
    val pattern = when (interval) {
        "15m" -> "HH:mm"
        "30m" -> "HH:mm"
        "1h" -> "HH:mm"
        "4h" -> "dd MMM HH:mm"
        "1d" -> "dd MMM"
        "1w"->"dd:MMM"
        "1M"->"MMM"
        else -> "HH:mm"
    }

    return object : CartesianValueFormatter {

        private val dateFormat =
            SimpleDateFormat(pattern, Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
            }

        override fun format(
            context: CartesianMeasuringContext,
            value: Double,
            verticalAxisPosition: Axis.Position.Vertical?,
        ): String {
            val index = value
                .toInt()
                .coerceIn(0, klines.lastIndex)

            val openTimeMillis = klines[index].openTime

            return dateFormat.format(Date(openTimeMillis))
        }
    }
}

fun calculateNiceStep(
    minY: Double,
    maxY: Double,
    targetTicks: Int = 5
): Double {
    val range = maxY - minY
    if (range <= 0.0) return 1.0

    val rawStep = range / targetTicks
    val exponent = floor(log10(rawStep))
    val power = 10.0.pow(exponent)

    val normalized = rawStep / power

    val niceNormalized = when {
        normalized <= 1.0 -> 1.0
        normalized <= 2.0 -> 2.0
        normalized <= 5.0 -> 5.0
        else -> 10.0
    }

    return niceNormalized * power
}
fun createVisibleYRangeProvider(
    visibleMinY: Double,
    visibleMaxY: Double
): CartesianLayerRangeProvider {
    val range = visibleMaxY - visibleMinY

    val padding = when {
        range <= 0.0 -> visibleMaxY * 0.001
        else -> range * 0.15
    }

    val min = visibleMinY - padding
    val max = visibleMaxY + padding

    return object : CartesianLayerRangeProvider {
        override fun getMinY(
            minY: Double,
            maxY: Double,
            extraStore: ExtraStore
        ): Double {
            return min
        }

        override fun getMaxY(
            minY: Double,
            maxY: Double,
            extraStore: ExtraStore
        ): Double {
            return max
        }
    }
}
val MarkerValueFormatter =
    DefaultCartesianMarker.ValueFormatter.default(DecimalFormat("$#,###.00"))