package com.example.coin_details

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.coin_details.databinding.FragmentCoinDetailsBinding
import com.example.coin_details.di.CoinDetailsFeatureInjector
import com.example.coin_details.ui.StartAxisValueFormatter
import com.example.coin_details.ui.calculateNiceStep
import com.example.coin_details.ui.createBottomAxisValueFormatter
import com.example.coin_details.ui.createVisibleYRangeProvider
import com.example.feature.live_notification.NotificationForegroundService

import com.example.model.Coin
import com.example.model.CoinKline
import com.example.navigation.AppNavigator
import com.example.ui.BaseFragment
import com.google.android.material.chip.Chip
import com.patrykandpatrick.vico.views.cartesian.Scroll
import com.patrykandpatrick.vico.views.cartesian.ScrollHandler
import com.patrykandpatrick.vico.views.cartesian.Zoom
import com.patrykandpatrick.vico.views.cartesian.ZoomHandler
import com.patrykandpatrick.vico.views.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.views.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.views.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.views.cartesian.data.candlestickModel
import com.patrykandpatrick.vico.views.cartesian.layer.CandlestickCartesianLayer
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoinDetailsFragment :
    BaseFragment<FragmentCoinDetailsBinding>(FragmentCoinDetailsBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: CoinsDetailsViewModelFactory

    @Inject
    lateinit var appNavigator: AppNavigator

    private val modelProducer = CartesianChartModelProducer()

    private val scrollHandler = ScrollHandler(
        initialScroll = Scroll.Absolute.End
    )

    private val zoomHandler = ZoomHandler(
        initialZoom = Zoom.fixed(6f),
        minZoom = Zoom.Content,
        maxZoom = Zoom.fixed(30f)
    )

    private val symbol: String by lazy {
        requireArguments().getString("symbol") ?: error("no symbol")
    }

    private val viewModel: CoinDetailsViewModel by viewModels {
        viewModelFactory.create(symbol)
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                val currentSymbol = viewModel.state.value.coinDetails.symbol

                if (currentSymbol.isNotBlank()) {
                    startLiveNotificationService(currentSymbol)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Notifications permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as CoinDetailsFeatureInjector)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChart()
        setupChip()
        setupClickListeners()

        viewModel.state.collectWithLifecycle { state ->
            render(state)
        }
    }

    private fun setupChart() {
        with(binding.chartView) {
            modelProducer = this@CoinDetailsFragment.modelProducer
            scrollHandler = this@CoinDetailsFragment.scrollHandler
            zoomHandler = this@CoinDetailsFragment.zoomHandler
        }
    }

    private fun setupChip() {
        binding.intervalButtons.check(R.id.m15)

        binding.intervalButtons.setOnCheckedStateChangeListener { group, chipIds ->
            val checkedId = chipIds.firstOrNull()
                ?: return@setOnCheckedStateChangeListener

            val chip = group.findViewById<Chip>(checkedId)
            val interval = chip.text.toString()

            viewModel.updateInterval(interval)
        }
    }

    private fun setupClickListeners() {
        binding.createAlert.setOnClickListener {
            val currentSymbol = viewModel.state.value.coinDetails.symbol

            if (currentSymbol.isNotBlank()) {
                appNavigator.openAlerts(this, currentSymbol)
            }
        }

        binding.favoriteButton.setOnClickListener {
            val coin = viewModel.state.value.coinDetails

            if (coin.symbol.isNotBlank()) {
                viewModel.toggleFavorite(coin)
            }
        }

        binding.notificationButton.setOnClickListener {
            val state = viewModel.state.value
            val currentSymbol = state.coinDetails.symbol
            val activeSymbol = state.currentNotificationSymbol

            if (currentSymbol.isBlank()) return@setOnClickListener

            if (activeSymbol == currentSymbol) {
                stopLiveNotificationService()
            } else {

                startLiveNotificationServiceWithPermissionCheck(currentSymbol)
            }
        }
    }

    private fun render(state: CoinDetailsUiState) {
        val klines = state.coinKline
        val coin = state.coinDetails

        if (klines.isNotEmpty()) {
            renderChart(
                klines = klines,
                interval = state.interval
            )

            setSymbolAndPrice(klines.last())
        }

        if (coin.symbol.isNotBlank()) {
            uploadImage(coin.baseAsset)
            renderFavoriteState(coin)
            renderNotificationButton(state)
        }
    }

    private fun renderChart(
        klines: List<CoinKline>,
        interval: String
    ) {
        val chartKlines = klines.takeLast(120)

        lifecycleScope.launch {
            updateChartStyle(
                klines = chartKlines,
                interval = interval
            )

            modelProducer.runTransaction {
                candlestickModel(
                    x = chartKlines.indices.toList(),
                    opening = chartKlines.map { it.openPrice },
                    closing = chartKlines.map { it.closePrice },
                    low = chartKlines.map { it.lowPrice },
                    high = chartKlines.map { it.highPrice }
                )
            }

            binding.chartView.post {
                binding.chartView.invalidate()
            }
        }
    }

    private fun updateChartStyle(
        klines: List<CoinKline>,
        interval: String
    ) {
        if (klines.isEmpty()) return

        val visibleMinY = klines.minOf { it.lowPrice }
        val visibleMaxY = klines.maxOf { it.highPrice }

        val yStep = calculateNiceStep(
            minY = visibleMinY,
            maxY = visibleMaxY,
            targetTicks = 5
        )

        val rangeProvider = createVisibleYRangeProvider(
            visibleMinY = visibleMinY,
            visibleMaxY = visibleMaxY
        )

        val oldChart = binding.chartView.chart ?: return

        binding.chartView.chart =
            oldChart.copy(
                (oldChart.layers[0] as CandlestickCartesianLayer).copy(
                    rangeProvider = rangeProvider
                ),
                startAxis =
                    (oldChart.startAxis as VerticalAxis).copy(
                        valueFormatter = StartAxisValueFormatter,
                        itemPlacer = VerticalAxis.ItemPlacer.step({ yStep })
                    ),
                bottomAxis =
                    (oldChart.bottomAxis as HorizontalAxis).copy(
                        valueFormatter = createBottomAxisValueFormatter(
                            interval = interval,
                            klines = klines
                        )
                    )
            )
    }

    private fun setSymbolAndPrice(kline: CoinKline) {
        binding.symbolText.text = kline.symbol
        binding.priceText.text = kline.closePrice.toString()
    }

    private fun uploadImage(symbol: String) {
        val coinSymbolLower = symbol.lowercase()

        val iconUrl =
            "https://raw.githubusercontent.com/spothq/cryptocurrency-icons/master/128/color/$coinSymbolLower.png"

        Glide.with(requireContext())
            .load(iconUrl)
            .placeholder(R.drawable.ic_coin_image_placeholder)
            .into(binding.coinImage)
    }

    private fun renderFavoriteState(coin: Coin) {
        binding.favoriteButton.setImageResource(
            if (coin.isFavorite) {
                R.drawable.ic_favorite_inline
            } else {
                R.drawable.ic_favorite_outline
            }
        )

        val colorRes = if (coin.isFavorite) {
            R.color.favorite_red
        } else {
            R.color.favorite_yellow
        }

        binding.favoriteButton.imageTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(binding.root.context, colorRes)
            )
    }

    private fun renderNotificationButton(state: CoinDetailsUiState) {
        val currentSymbol = state.coinDetails.symbol
        val activeSymbol = state.currentNotificationSymbol

        val isCurrentCoinRunning =
            currentSymbol.isNotBlank() && currentSymbol == activeSymbol

        binding.notificationButton.text =
            if (isCurrentCoinRunning) {
                "Stop notification"
            } else {
                "Start notification"
            }

        binding.notificationButton.isSelected = isCurrentCoinRunning
    }

    private fun startLiveNotificationServiceWithPermissionCheck(symbol: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                startLiveNotificationService(symbol)
            } else {
                requestNotificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        } else {
            startLiveNotificationService(symbol)
        }
    }

    private fun startLiveNotificationService(symbol: String) {
        val intent = Intent(
            requireContext(),
            NotificationForegroundService::class.java
        ).apply {
            putExtra(NotificationForegroundService.EXTRA_SYMBOL, symbol)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    private fun stopLiveNotificationService() {
        val intent = Intent(
            requireContext(),
            NotificationForegroundService::class.java
        )
        requireContext().stopService(intent)
    }
}