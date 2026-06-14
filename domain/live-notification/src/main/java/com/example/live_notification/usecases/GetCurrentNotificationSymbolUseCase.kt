package com.example.live_notification.usecases

import com.example.live_notification.repository.LiveNotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentNotificationSymbolUseCase @Inject constructor(private val repository: LiveNotificationRepository) {
    operator fun invoke(): Flow<String> {
        return repository.getCurrentLiveNotificationSymbol()
    }
}