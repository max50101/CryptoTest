package com.example.domain.coins.usecases

import com.example.domain.coins.repository.CoinsRepository
import com.example.model.Coin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCoinDetailsUseCase @Inject constructor(private val repo: CoinsRepository) {
    suspend operator  fun invoke(symbol:String): Flow<Coin> {
        return repo.observeCoinDetails(symbol)
    }
}