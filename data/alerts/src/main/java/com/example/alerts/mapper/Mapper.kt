package com.example.alerts.mapper

import com.example.database.entity.AlertConditionTypeEntity
import com.example.database.entity.AlertEntity
import com.example.database.entity.AlertStatusEntity
import com.example.model.Alert
import com.example.model.AlertStatus
import com.example.model.ConditionsType

fun AlertEntity.toAlert(): Alert {
    return Alert(
        id = id,
        symbol = symbol,
        conditionType = direction.toDomain(),
        targetPrice = targetPrice,
        status = status.toDomain(),
        createdAt = createdAt
    )
}

fun Alert.toAlertEntity(): AlertEntity {
    return AlertEntity(
        id = id,
        symbol = symbol,
        direction = conditionType.toEntity(),
        targetPrice = targetPrice,
        status = status.toEntity(),
        createdAt = createdAt
    )
}

private fun AlertConditionTypeEntity.toDomain(): ConditionsType {
    return when (this) {
        AlertConditionTypeEntity.ABOVE -> ConditionsType.ABOVE
        AlertConditionTypeEntity.BELOW -> ConditionsType.BELLOW
    }
}

private fun ConditionsType.toEntity(): AlertConditionTypeEntity {
    return when (this) {
        ConditionsType.ABOVE -> AlertConditionTypeEntity.ABOVE
        ConditionsType.BELLOW -> AlertConditionTypeEntity.BELOW
    }
}

private fun AlertStatusEntity.toDomain(): AlertStatus {
    return when (this) {
        AlertStatusEntity.ACTIVE -> AlertStatus.ACTIVE
        AlertStatusEntity.TRIGGERED -> AlertStatus.TRIGGERED
    }
}

public fun AlertStatus.toEntity(): AlertStatusEntity {
    return when (this) {
        AlertStatus.ACTIVE -> AlertStatusEntity.ACTIVE
        AlertStatus.TRIGGERED -> AlertStatusEntity.TRIGGERED
        else-> AlertStatusEntity.TRIGGERED
    }
}
