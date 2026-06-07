package com.example.domain.coins.usecases

import com.example.domain.coins.repository.CoinsRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(private val coinsRepo: CoinsRepository) {
    suspend operator fun invoke(symbol:String){
        coinsRepo.toggleFavorite(symbol)
    }
}