package com.example.domain.coins.usecases

import com.example.domain.coins.repository.CoinsRepository
import javax.inject.Inject

class RefreshCoinsUseCase @Inject constructor(private val repository: CoinsRepository) {
    suspend operator fun invoke(){
        repository.refreshCoins();
    }
}