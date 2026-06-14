package com.example.coin_alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alerts.usecases.InsertAlertUseCase
import javax.inject.Inject

class AlertBottomSheetViewModelFactory @Inject constructor(private val insertAlertUseCase: InsertAlertUseCase ) {
    @Suppress("UNCHECKED_CAST")
    fun create(symbol:String): ViewModelProvider.Factory{
        return object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AlertBottomSheetViewModel(insertAlertUseCase,symbol) as T
            }
        }
    }
}