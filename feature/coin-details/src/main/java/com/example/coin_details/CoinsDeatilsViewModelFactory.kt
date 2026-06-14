package com.example.coin_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.domain.coins.usecases.ObserveCoinChartUseCase
import com.example.domain.coins.usecases.ObserveCoinDetailsUseCase
import com.example.domain.coins.usecases.ToggleFavoriteUseCase
import com.example.live_notification.usecases.GetCurrentNotificationSymbolUseCase
import com.example.live_notification.usecases.SetCurrentNotificationSymbolUseCase
import javax.inject.Inject

class CoinsDetailsViewModelFactory @Inject constructor(
    private val observeCoinChartUseCase: ObserveCoinChartUseCase,
    private val observeCoinDetailsUseCase: ObserveCoinDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getCurrentNotificationSymbolUseCase: GetCurrentNotificationSymbolUseCase,
    private val setCurrentNotificationSymbolUseCase: SetCurrentNotificationSymbolUseCase
) {

    @Suppress("UNCHECKED_CAST")
    fun create(symbol: String): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val savedStateHandle=extras.createSavedStateHandle()
                return CoinDetailsViewModel(
                    observeCoinChartUseCase,
                    observeCoinDetailsUseCase,
                    toggleFavoriteUseCase,
                    getCurrentNotificationSymbolUseCase,
                    setCurrentNotificationSymbolUseCase,
                    symbol,
                    savedStateHandle
                ) as T
            }
        }
    }
}