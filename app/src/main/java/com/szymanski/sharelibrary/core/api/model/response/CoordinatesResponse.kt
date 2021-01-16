package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate

data class CoordinatesResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
) {
    fun toCoordinates() = Coordinate(
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude
    )

    companion object
}