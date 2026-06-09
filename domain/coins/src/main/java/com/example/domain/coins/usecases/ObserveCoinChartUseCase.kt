package com.example.domain.coins.usecases

import com.example.domain.coins.repository.CoinsRepository
import com.example.model.Coin
import com.example.model.CoinKline
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCoinDetailsUseCase @Inject constructor(private val coinsRepository: CoinsRepository) {
    public operator fun invoke(symbol:String, interval:String): Flow<List<CoinKline>> {
        return coinsRepository.observeCoinDetails(symbol,interval)
    }
}