package com.example.market_scan

import android.content.Context
import android.content.pm.PackageManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.feature.market_scan.databinding.FragmentMarketScanBinding
import com.example.market_scan.di.MarketScanInjector
import com.example.ui.BaseFragment
import javax.inject.Inject

class MarketScanFragment() :
    BaseFragment<FragmentMarketScanBinding>(FragmentMarketScanBinding::inflate) {


    @Inject
    lateinit var viewModelFactory: MarketScanViewModelFactory
    private val requestPermissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){granted->
        if (granted) {
            Toast.makeText(
                requireContext(),
                "Starting binding",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.onScreenStarted()
        } else {
            Toast.makeText(
                requireContext(),
                "Notifications permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

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
        if(ContextCompat.checkSelfPermission(requireContext(),"com.example.scan_engine.permission.ACCESS_MARKET_SCAN")== PackageManager.PERMISSION_GRANTED){
            viewModel.onScreenStarted()
        }else{
            requestPermissionLauncher.launch("com.example.scan_engine.permission.ACCESS_MARKET_SCAN")
        }

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
