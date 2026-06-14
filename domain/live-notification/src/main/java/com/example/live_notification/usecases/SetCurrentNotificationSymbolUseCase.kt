package com.example.live_notification.usecases

import com.example.live_notification.repository.LiveNotificationRepository
import javax.inject.Inject

class SetCurrentNotificationSymbolUseCase @Inject constructor(private val liveNotificationRepository: LiveNotificationRepository) {
    suspend operator fun invoke(symbol:String){
        liveNotificationRepository.setCurrentNotificationSymbol(symbol)
    }
}