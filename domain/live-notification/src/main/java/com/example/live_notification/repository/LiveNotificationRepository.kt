package com.example.live_notification.repository

import kotlinx.coroutines.flow.Flow

interface LiveNotificationRepository {
    suspend fun setCurrentNotificationSymbol(symbol:String)
    fun getCurrentLiveNotificationSymbol(): Flow<String>
    suspend fun clear()
}