    package com.example.coin_list

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.domain.coins.usecases.ObserveCoinsUseCase
    import com.example.domain.coins.usecases.RefreshCoinsUseCase
    import com.example.domain.coins.usecases.ToggleFavoriteUseCase
    import com.example.model.Coin
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.SharingStarted
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.flow.catch
    import kotlinx.coroutines.flow.combine
    import kotlinx.coroutines.flow.debounce
    import kotlinx.coroutines.flow.distinctUntilChanged
    import kotlinx.coroutines.flow.onStart
    import kotlinx.coroutines.flow.stateIn
    import kotlinx.coroutines.flow.update
    import kotlinx.coroutines.launch
    import kotlin.collections.emptyList

    class CoinListViewModel(private val refreshCoinsUseCase: RefreshCoinsUseCase,
                            private val observeCoinsUseCase: ObserveCoinsUseCase,
                            private val toggleFavoriteUseCase: ToggleFavoriteUseCase
    ) : ViewModel() {

        private val _isLoading=MutableStateFlow(false)
        private val _error= MutableStateFlow<String?>(null)
        private val _searchQuarry= MutableStateFlow("")
        private val _coins=MutableStateFlow<List<Coin>>(emptyList())
        val state: StateFlow<CoinListUiState> = combine(
            _isLoading, _coins, _searchQuarry.debounce(300).distinctUntilChanged(),_error,
        ){loading,coins, filter, error->
            val filteredCoins= if(filter.isBlank()){
                coins
            }else{
                coins.filter{it.symbol.contains(filter,ignoreCase = true)}
            }
            CoinListUiState(
                loading, filteredCoins, filter, error
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CoinListUiState(isLoading = true)
        )

        init {
            observeCoins()
            refreshCoins()
        }

        private fun observeCoins(){
            viewModelScope.launch {
                observeCoinsUseCase().onStart {
                    _isLoading.value=true
                }.catch {error-> _error.value=error.message
                }.collect { coins-> _coins.value=coins }
            }
        }

         private fun refreshCoins(){
            viewModelScope.launch {
                runCatching {
                    refreshCoinsUseCase()
                }.onFailure{throwable->
                    _isLoading.value=false
                    _error.value=throwable.message
                }
            }
        }

        fun toggleFavorite(coin:Coin){
            viewModelScope.launch {
                runCatching {
                    toggleFavoriteUseCase(coin.symbol)
                }.onFailure{throwable->
                    _isLoading.value=false
                    _error.value=throwable.message
                }
            }
        }

        fun search(text:String){
            _searchQuarry.value=text
        }

    }

    data class CoinListUiState(val isLoading: Boolean=false,
                               val coins:List<Coin> = emptyList(),
                               val searchQuarry:String?= null,
                               val error: String?=null)