package com.szymanski.sharelibrary.core.api.model.request

import com.szymanski.sharelibrary.features.user.domain.model.Coordinate

data class CoordinatesRequest(
    val latitude: Double?,
    val longitude: Double?,
) {
    constructor(coordinate: Coordinate) : this(
        latitude = coordinate.latitude,
        longitude = coordinate.longitude
    )
}