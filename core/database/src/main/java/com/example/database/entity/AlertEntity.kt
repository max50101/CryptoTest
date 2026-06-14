package com.example.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity("alerts", foreignKeys = [ForeignKey(  entity = CoinsEntity::class,
    parentColumns = ["symbol"],
    childColumns = ["symbol"],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE)])
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id:Long=0,
    val symbol:String,
    val direction: AlertConditionTypeEntity,
    val targetPrice: Double,
    val status: AlertStatusEntity = AlertStatusEntity.ACTIVE,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AlertConditionTypeEntity{
    ABOVE,
    BELOW
}

enum class AlertStatusEntity{
    TRIGGERED,
    ACTIVE
}
