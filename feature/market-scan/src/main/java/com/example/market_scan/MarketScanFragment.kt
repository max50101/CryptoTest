package com.example.market_scan

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import com.example.feature.market_scan.databinding.FragmentMarketScanBinding
import com.example.market_scan.di.MarketScanInjector
import com.example.ui.BaseFragment
import javax.inject.Inject

class MarketScanFragment() :
    BaseFragment<FragmentMarketScanBinding>(FragmentMarketScanBinding::inflate) {


    @Inject
    lateinit var viewModelFactory: MarketScanViewModelFactory

    private val viewModel: MarketScanViewModel by viewModels {
        viewModelFactory
    }

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
        binding.startScanButton.setOnClickListener {
            viewModel.startScan()
            binding.cancelScanButton.visibility = View.VISIBLE
            binding.startScanButton.visibility = View.GONE
        }

        binding.cancelScanButton.setOnClickListener {
            viewModel.cancelScan()
            binding.cancelScanButton.visibility = View.INVISIBLE
            binding.startScanButton.visibility = View.GONE
        }
    }
}
