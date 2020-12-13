package com.szymanski.sharelibrary.features.user.presentation.model

import android.os.Parcelable
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoordinateDisplayable(
    val id: Long?,
    val latitude: Double?,
    val longitude: Double?,
) : Parcelable {
    constructor(coordinate: Coordinate) : this(
        id = coordinate.id,
        latitude = coordinate.latitude,
        longitude = coordinate.longitude
    )

    fun toCoordinate() = Coordinate(
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude
    )
}