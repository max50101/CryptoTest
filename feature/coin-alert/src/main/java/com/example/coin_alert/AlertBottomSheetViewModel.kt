package com.example.coin_alert

import android.os.Message
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alerts.usecases.GetAlertsUseCase
import com.example.alerts.usecases.InsertAlertUseCase
import com.example.model.Alert
import com.example.model.ConditionsType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class AlertBottomSheetViewModel(
    private val insertAlertUseCase: InsertAlertUseCase,
    private val symbol:String
) : ViewModel() {
    private val _events= MutableSharedFlow<AlertBottomSheetEvent>()
    val events=_events.asSharedFlow()
    private var selectedTargetPrice: String=""
    private var selectedDirection: ConditionsType= ConditionsType.ABOVE
    init {

    }
    fun insertAlert(){
        val targetPrice = selectedTargetPrice.toDoubleOrNull()

        if (targetPrice == null || targetPrice <= 0.0) {
            viewModelScope.launch {
                _events.emit(
                    AlertBottomSheetEvent.ShowToast("Enter valid target price")
                )
            }
            return
        }
        viewModelScope.launch {
            runCatching {
                insertAlertUseCase(
                    Alert(
                        symbol = symbol,
                        conditionType = selectedDirection,
                        targetPrice = targetPrice,

                    )
                )
            }.onSuccess {
                _events.emit(AlertBottomSheetEvent.ShowToast("Successfully created alert"))
                _events.emit(AlertBottomSheetEvent.CloseBottomSheet)
            }.onFailure { throwable ->
                _events.emit(AlertBottomSheetEvent.ShowToast("error ${throwable.message}"))
                 Log.e("AllertBottomSheetViewModel","Failed to created alert",throwable)
            }
        }
    }

    fun updateTargetPrice(text:String){
        selectedTargetPrice=text.trim()
    }

    fun updateDirection(text:String){
        selectedDirection= when(text){
            "Up"-> ConditionsType.ABOVE
            "Down"-> ConditionsType.BELLOW
            else -> ConditionsType.ABOVE
        }
    }
}

sealed class AlertBottomSheetEvent(){
    data class ShowToast(val message: String): AlertBottomSheetEvent()
    data object CloseBottomSheet : AlertBottomSheetEvent()

}