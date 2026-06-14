package com.example.coin_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.coins.usecases.ObserveCoinChartUseCase
import com.example.domain.coins.usecases.ObserveCoinDetailsUseCase
import com.example.domain.coins.usecases.ToggleFavoriteUseCase
import com.example.live_notification.usecases.GetCurrentNotificationSymbolUseCase
import com.example.live_notification.usecases.SetCurrentNotificationSymbolUseCase
import com.example.model.Coin
import com.example.model.CoinKline
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class CoinDetailsViewModel(
    private val observeCoinChartUseCase: ObserveCoinChartUseCase,
    private val observeCoinDetailsUseCase: ObserveCoinDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getCurrentNotificationSymbolUseCase: GetCurrentNotificationSymbolUseCase,
    private val setCurrentNotificationSymbolUseCase: SetCurrentNotificationSymbolUseCase,
    private val symbol: String,
    private val savedStateHandler: SavedStateHandle
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    private val _coinDetails = MutableStateFlow<Coin>(Coin())
    private val _coinKline = MutableStateFlow<List<CoinKline>>(emptyList())
    private val _interval = MutableStateFlow<String>("15m")
    private val _currentNotificationSymbol = MutableStateFlow<String>("")

    val state: StateFlow<CoinDetailsUiState> = combine(
        _isLoading, _coinDetails, _coinKline, _interval, _currentNotificationSymbol
    ) { loading, coinDetails, coinKline, interval, currentNotificationSymbol ->
        CoinDetailsUiState(loading, coinDetails, coinKline, interval, currentNotificationSymbol)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CoinDetailsUiState(isLoading = true)
    )
    private val _events = MutableSharedFlow<CoinsUiEvent>()
    val events = _events.asSharedFlow()
    private var observeJob: Job? = null

    init {
        observe()
        getDetails()
        getCurrentNotificationSymbol()
        addCloseable {
            observeJob?.cancel()
        }
    }

    private fun observe() {
        observeJob?.cancel()
        observeJob = observeCoinChartUseCase(symbol, state.value.interval)
            .onStart { _isLoading.value = true }
            .onEach { klines ->
                _coinKline.value = klines
                _isLoading.value = false
            }.catch { throwable ->
                _events.emit(CoinsUiEvent.SendToast(message = throwable.message ?: "Unknown error"))
            }.launchIn(viewModelScope)

    }

    private fun getDetails() {
        viewModelScope.launch {
            observeCoinDetailsUseCase(symbol).onStart {
                _isLoading.value = true
            }.catch { throwable ->
                _events.emit(CoinsUiEvent.SendToast(message = throwable.message ?: "Unknown error"))
            }.collect { coin ->
                _coinDetails.value = coin
                _isLoading.value = false
            }
        }
    }

    private fun getCurrentNotificationSymbol() {
        viewModelScope.launch {
            getCurrentNotificationSymbolUseCase()
                .catch { throwable ->
                    _events.emit(
                        CoinsUiEvent.SendToast(
                            message = throwable.message ?: "Unknown error"
                        )
                    )
                }
                .collect { symbol ->
                    _currentNotificationSymbol.value = symbol
                }
        }
    }

    fun toggleFavorite(coin: Coin) {
        viewModelScope.launch {
            toggleFavoriteUseCase(coin.symbol)
        }
    }

    fun updateInterval(interval: String) {
        _interval.value = interval
        observe()
    }

    fun setCurrentNotificationSymbol() {
        viewModelScope.launch {
            setCurrentNotificationSymbolUseCase(symbol)
        }
    }


}

data class CoinDetailsUiState(
    val isLoading: Boolean = false,
    val coinDetails: Coin = Coin(),
    val coinKline: List<CoinKline> = emptyList(),
    val interval: String = "15m", val currentNotificationSymbol: String = ""
)

sealed class CoinsUiEvent {
    data class SendToast(val message: String) : CoinsUiEvent()

}