package com.example.coins

import android.content.Context
import android.health.connect.datatypes.units.Length
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coins.databinding.FragmentCoinsBinding
import com.example.coins.di.CoinsFeatureInjector
import com.example.coins.ui.CoinsAdapter
import com.example.ui.BaseFragment
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CoinsFragment(
) : BaseFragment<FragmentCoinsBinding>(FragmentCoinsBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: CoinsViewModelFactory
    private val adapter= CoinsAdapter()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as CoinsFeatureInjector)
            .inject(this)
    }

    private val viewModel: CoinsViewModel by viewModels() {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.coinRecyclerView.adapter=adapter
        binding.coinRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        viewModel.state.collectWithLifecycle { state -> render(state) }
        viewModel.events.collectWithLifecycle { event -> handle(event) }
    }

    private fun render(state: CoinsUiState) {
        if (!state.coins.isEmpty()) {
            adapter.submitList(state.coins)
        }

    }

    private fun handle(event: CoinsUiEvens) {
        when (event) {
            is CoinsUiEvens.SendToast ->
                Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_LONG)
                .show()
        }
    }

}