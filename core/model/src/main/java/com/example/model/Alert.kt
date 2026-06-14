package com.example.model

data class Alert(
    val id: Long=0,
    val symbol: String,
    val conditionType: ConditionsType,
    val targetPrice: Double,
    val status: AlertStatus= AlertStatus.ACTIVE,
    val createdAt: Long= System.currentTimeMillis()
)

enum class  AlertStatus{
    ACTIVE,
    TRIGGERED,
    CANCELED
}

enum class ConditionsType{
    ABOVE,
    BELLOW
}
