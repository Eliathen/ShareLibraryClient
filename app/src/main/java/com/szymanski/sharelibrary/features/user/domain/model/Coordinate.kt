package com.szymanski.sharelibrary.features.user.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordinate(
    val id: Long?,
    val latitude: Double?,
    val longitude: Double?,
) : Parcelable {
    companion object
}