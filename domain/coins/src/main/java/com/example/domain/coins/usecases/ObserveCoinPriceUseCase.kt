package com.example.domain.coins.usecases

import com.example.domain.coins.repository.CoinsRepository
import com.example.model.Coin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCoinPriceUseCase @Inject constructor(private val repository: CoinsRepository) {
    public operator fun invoke(symbol:String): Flow<Coin> {
        return repository.observeCoinPrice(symbol)
    }
}