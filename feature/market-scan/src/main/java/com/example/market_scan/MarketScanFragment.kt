package com.example.market_scan

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feature.market_scan.databinding.FragmentMarketScanBinding
import com.example.market_scan.di.MarketScanInjector
import com.example.market_scan.model.MarketScanState
import com.example.market_scan.model.MarketSignal
import com.example.market_scan.model.ScannerConnectionState
import com.example.market_scan.ui.MarketSignalsAdapter
import com.example.ui.BaseFragment
import javax.inject.Inject

class MarketScanFragment() :
    BaseFragment<FragmentMarketScanBinding>(FragmentMarketScanBinding::inflate) {


    @Inject
    lateinit var viewModelFactory: MarketScanViewModelFactory

    private val signalsAdapter: MarketSignalsAdapter by lazy {
        MarketSignalsAdapter()
    }

    private val viewModel: MarketScanViewModel by viewModels {
        viewModelFactory
    }
    private var currentSignals: List<MarketSignal> = emptyList()
    private var searchQuery: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MarketScanInjector)
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onScreenStarted()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signalsRecyclerView.adapter = signalsAdapter
        binding.signalsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            searchQuery = text?.toString().orEmpty()
            renderSignals()
        }

        binding.startScanButton.setOnClickListener {
            viewModel.startScan()
            binding.cancelScanButton.visibility = View.VISIBLE
            binding.startScanButton.visibility = View.GONE
        }

        binding.cancelScanButton.setOnClickListener {
            viewModel.cancelScan()
            binding.cancelScanButton.visibility = View.GONE
            binding.startScanButton.visibility = View.VISIBLE
        }

        viewModel.connectionState.collectWithLifecycle { state ->
            renderConnectionState(state)
        }
        viewModel.scanState.collectWithLifecycle { state ->
            renderScanState(state)
        }
    }

    private fun renderConnectionState(state: ScannerConnectionState) {
        binding.startScanButton.isEnabled = state == ScannerConnectionState.Connected
        binding.connectionText.text = when (state) {
            ScannerConnectionState.Connected -> "Connected"
            ScannerConnectionState.Connecting -> "Connecting"
            ScannerConnectionState.Disconnected -> "Disconnected"
            ScannerConnectionState.NotInstalled -> "Scanner not installed"
            is ScannerConnectionState.Error -> state.message
        }
    }

    private fun renderScanState(state: MarketScanState) {
        when (state) {
            MarketScanState.Idle -> {
                currentSignals = emptyList()
                binding.progressText.text = "Ready"
                binding.progressIndicator.progress = 0
                binding.startScanButton.visibility = View.VISIBLE
                binding.cancelScanButton.visibility = View.GONE
            }

            is MarketScanState.Running -> {
                currentSignals = state.signals
                binding.progressText.text = buildString {
                    append("Progress: ")
                    append(state.processed)
                    append(" / ")
                    append(state.total)
                    state.currentSymbol?.let { symbol ->
                        append(" - ")
                        append(symbol)
                    }
                }
                binding.progressIndicator.progress = state.progressPercent
                binding.startScanButton.visibility = View.GONE
                binding.cancelScanButton.visibility = View.VISIBLE
            }

            is MarketScanState.Completed -> {
                currentSignals = state.signals
                binding.progressText.text = "Completed"
                binding.progressIndicator.progress = 100
                binding.startScanButton.visibility = View.VISIBLE
                binding.cancelScanButton.visibility = View.GONE
            }

            is MarketScanState.Error -> {
                binding.progressText.text = state.message
                binding.progressIndicator.progress = 0
                binding.startScanButton.visibility = View.VISIBLE
                binding.cancelScanButton.visibility = View.GONE
            }

            is MarketScanState.Cancelled -> {
                binding.progressText.text = "Cancelled"
                binding.progressIndicator.progress = 0
                binding.startScanButton.visibility = View.VISIBLE
                binding.cancelScanButton.visibility = View.GONE
            }
        }
        renderSignals()
    }

    private fun renderSignals() {
        val filteredSignals = if (searchQuery.isBlank()) {
            currentSignals
        } else {
            currentSignals.filter { signal ->
                signal.symbol.contains(searchQuery, ignoreCase = true) ||
                    signal.type.name.contains(searchQuery, ignoreCase = true) ||
                    signal.description.contains(searchQuery, ignoreCase = true)
            }
        }

        binding.signalCountText.text = filteredSignals.size.toString()
        signalsAdapter.submitList(filteredSignals)
        binding.signalsRecyclerView.visibility = if (filteredSignals.isEmpty()) View.GONE else View.VISIBLE
        binding.emptyText.visibility = if (filteredSignals.isEmpty()) View.VISIBLE else View.GONE
        binding.emptyText.text = if (currentSignals.isEmpty()) {
            getString(com.example.feature.market_scan.R.string.market_scan_empty)
        } else {
            getString(com.example.feature.market_scan.R.string.market_scan_no_matches)
        }
    }
}
