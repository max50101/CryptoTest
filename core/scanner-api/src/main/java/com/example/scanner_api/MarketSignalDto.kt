package com.example.scanner_api

import android.os.Parcel
import android.os.Parcelable

data class MarketSignalDto(
    val symbol: String,
    val type: String,
    val description: String,
    val score: Int,
    val priceChangePercent: Double,
    val volumeSpikeMultiplier: Double
) : Parcelable {


    constructor(parcel: Parcel) : this(
        symbol = parcel.readString() ?: "",
        type = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        score = parcel.readInt(),
        priceChangePercent = parcel.readDouble(),
        volumeSpikeMultiplier = parcel.readDouble()
    )

    override fun describeContents(): Int {
        return 0
    }


    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(symbol)
        dest.writeString(type)
        dest.writeString(description)
        dest.writeInt(score)
        dest.writeDouble(priceChangePercent)
        dest.writeDouble(volumeSpikeMultiplier)
    }

    companion object CREATOR : Parcelable.Creator<MarketSignalDto> {
        override fun createFromParcel(parcel: Parcel): MarketSignalDto {
            return MarketSignalDto(parcel)
        }

        override fun newArray(size: Int): Array<MarketSignalDto?> {
            return arrayOfNulls(size)
        }
    }
}