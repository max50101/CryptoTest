package com.example.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.coins.usecases.ObserveFavoriteCoinsUseCase
import javax.inject.Inject


class CoinsViewModelFactory @Inject constructor(
    private val observeFavoriteCoinsUseCase: ObserveFavoriteCoinsUseCase
) : ViewModelProvider.Factory{


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CoinsViewModel(observeFavoriteCoinsUseCase) as T
    }
}