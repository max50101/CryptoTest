package com.example.database.utils

import androidx.room.TypeConverter

import com.example.database.entity.AlertConditionTypeEntity
import com.example.database.entity.AlertStatusEntity

class AlertTypeConverter {
    @TypeConverter
    fun fromConditionType(alertConditionType: AlertConditionTypeEntity):String{
        return alertConditionType.name
    }

    @TypeConverter
    fun toConditionType(value:String): AlertConditionTypeEntity{
        return AlertConditionTypeEntity.valueOf(value)
    }

    @TypeConverter
    fun fromAlertStatus(status: AlertStatusEntity): String{
        return status.name
    }

    @TypeConverter
    fun toAlertStatus(value:String): AlertStatusEntity{
        return AlertStatusEntity.valueOf(value)
    }
}