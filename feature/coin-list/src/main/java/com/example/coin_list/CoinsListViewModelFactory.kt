package com.example.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.coins.usecases.ObserveCoinsUseCase
import com.example.domain.coins.usecases.RefreshCoinsUseCase
import com.example.domain.coins.usecases.ToggleFavoriteUseCase
import javax.inject.Inject

class CoinsListViewModelFactory @Inject constructor(private val refreshCoinsUseCase: RefreshCoinsUseCase,
                                                    private val observeCoinsUseCase: ObserveCoinsUseCase,
                                                    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
        )
    : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoinListViewModel(refreshCoinsUseCase =refreshCoinsUseCase,
            observeCoinsUseCase = observeCoinsUseCase,
            toggleFavoriteUseCase =toggleFavoriteUseCase ) as T
    }
}