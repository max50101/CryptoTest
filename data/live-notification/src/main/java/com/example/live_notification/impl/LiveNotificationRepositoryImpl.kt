package com.example.live_notification.impl

import com.example.datastore.ForegroundServiceDataStore
import com.example.live_notification.repository.LiveNotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LiveNotificationRepositoryImpl @Inject constructor(private val store: ForegroundServiceDataStore) :
    LiveNotificationRepository {
    override suspend fun setCurrentNotificationSymbol(symbol: String) {
        store.setCurrentServiceSymbol(symbol)
    }

    override fun getCurrentLiveNotificationSymbol(): Flow<String> {
        return store.getCurrentServiceSymbol()
    }

    override suspend fun clear() {
        store.clearActiveSymbol()
    }

}