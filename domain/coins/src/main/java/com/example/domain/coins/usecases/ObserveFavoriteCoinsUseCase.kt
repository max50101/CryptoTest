package com.example.domain.coins.usecases

import com.example.domain.coins.repository.CoinsRepository
import com.example.model.Coin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteCoinsUseCase @Inject constructor(private val repository: CoinsRepository) {
    operator fun invoke(): Flow<List<Coin>> {
        return repository.observeFavoriteCoins()
    }
}