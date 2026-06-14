package com.example.coin_list

import android.app.Application
import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coin_list.databinding.FragmentCoinListBinding
import com.example.coin_list.di.CoinsListFeatureInjector
import com.example.coin_list.ui.CoinsListAdapter
import com.example.model.Coin
import com.example.navigation.AppNavigator
import com.example.ui.BaseFragment
import javax.inject.Inject

class CoinListFragment : BaseFragment<FragmentCoinListBinding>(FragmentCoinListBinding::inflate) {
    @Inject
    lateinit var appNavigator: AppNavigator

    @Inject
    lateinit var viewModelFactory: CoinsListViewModelFactory
    private val viewModel: CoinListViewModel by viewModels {
        viewModelFactory
    }
    private val adapter: CoinsListAdapter by lazy {
        CoinsListAdapter({ coin -> viewModel.toggleFavorite(coin) },
            { coin -> startCoinDetails(coin) })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as CoinsListFeatureInjector)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.coinsList.adapter = adapter
        binding.coinsList.layoutManager = LinearLayoutManager(requireContext())
        binding.coinsList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.search(text?.toString().orEmpty())
        }
        viewModel.state.collectWithLifecycle { state ->
            render(state)
        }
    }

    private fun render(state: CoinListUiState) {
        if (!state.coins.isEmpty()) {
            adapter.submitList(state.coins)
        }
    }

    private fun startCoinDetails(coin: Coin) {
        appNavigator.openCoinDetails(findNavController(), coin.symbol)
    }


}