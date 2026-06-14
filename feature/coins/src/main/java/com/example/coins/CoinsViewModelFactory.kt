package com.example.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.domain.coins.usecases.ObserveFavoriteCoinsUseCase
import javax.inject.Inject
import kotlin.reflect.KClass


class CoinsViewModelFactory @Inject constructor(
    private val observeFavoriteCoinsUseCase: ObserveFavoriteCoinsUseCase
) : ViewModelProvider.Factory{



    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        val savedStateHandle=extras.createSavedStateHandle()
        return CoinsViewModel(observeFavoriteCoinsUseCase,savedStateHandle) as T
    }
}