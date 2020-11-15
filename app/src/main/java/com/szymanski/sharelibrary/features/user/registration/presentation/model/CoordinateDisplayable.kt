package com.szymanski.sharelibrary.features.user.registration.presentation.model

import com.szymanski.sharelibrary.features.user.domain.model.Coordinate

data class CoordinateDisplayable(
    val id: Long?,
    val latitude: Double?,
    val longitude: Double?,
) {
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