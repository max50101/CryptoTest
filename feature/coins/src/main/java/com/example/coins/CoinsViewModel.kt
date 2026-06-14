package com.example.coins

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.coins.usecases.ObserveFavoriteCoinsUseCase
import com.example.model.Coin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CoinsViewModel(
    private val observeFavoriteCoinsUseCase: ObserveFavoriteCoinsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state= MutableStateFlow(CoinsUiState())
    val state: StateFlow<CoinsUiState> = _state.asStateFlow()

    private val _events= MutableSharedFlow<CoinsUiEvens>()
    val events: SharedFlow<CoinsUiEvens> =_events.asSharedFlow()

    init {
        observeCoins()
    }
    fun observeCoins(){
        viewModelScope.launch {
           observeFavoriteCoinsUseCase().onStart {
               _state.update { currentState-> currentState.copy(isLoading = true) }
           }.catch { throwable->
               _state.update{ currentState->
                   currentState.copy(isLoading = false)}
               _events.emit(CoinsUiEvens.SendToast(throwable.message?: "UnknownError"))
           }.collect { coins -> _state.update { currentState-> currentState.copy(isLoading = false,coins=coins, errorMessage = null) } }
        }
    }



}

data class CoinsUiState(val isLoading: Boolean=false,val coins:List<Coin> =emptyList(), val errorMessage:String? =null)
sealed class CoinsUiEvens {
    data class SendToast(val message: String): CoinsUiEvens()
}