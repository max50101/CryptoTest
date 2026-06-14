package com.example.live_notification.usecases

import com.example.live_notification.repository.LiveNotificationRepository
import javax.inject.Inject

class ClearNotificationSymbolUseCase @Inject constructor(private val repooLiveNotificationRepository: LiveNotificationRepository)
{
    suspend operator fun invoke(){
        repooLiveNotificationRepository.clear()
    }
}