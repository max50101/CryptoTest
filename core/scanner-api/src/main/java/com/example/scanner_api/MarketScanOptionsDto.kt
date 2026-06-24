package com.example.scanner_api

import android.os.Parcelable

import android.os.Parcel

data class MarketScanOptionsDto(
    val quoteAsset: String,
    val interval: String,
    val candleLimits: Int,
    val maxSymbols: Int,
) : Parcelable {


    constructor(parcel: Parcel) : this(
        quoteAsset = parcel.readString() ?: "",
        interval = parcel.readString() ?: "",
        candleLimits = parcel.readInt(),
        maxSymbols = parcel.readInt(),

    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(quoteAsset)
        parcel.writeString(interval)
        parcel.writeInt(candleLimits)
        parcel.writeInt(maxSymbols)

    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<MarketScanOptionsDto> {
        override fun createFromParcel(parcel: Parcel): MarketScanOptionsDto {
            return MarketScanOptionsDto(parcel)
        }

        override fun newArray(size: Int): Array<MarketScanOptionsDto?> {
            return arrayOfNulls(size)
        }
    }
}
