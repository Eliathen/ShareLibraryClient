package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.user.domain.model.Address

data class AddressResponse(
    @SerializedName("buildingNumber") val buildingNumber: String,
    @SerializedName("city") val city: String,
    @SerializedName("coordinates") val coordinates: CoordinatesResponse,
    @SerializedName("country") val country: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("id") val id: Long,
    @SerializedName("street") val street: String,
) {
    fun toAddress() = Address(
        country = this.country,
        city = this.city,
        postalCode = this.postalCode,
        id = this.id,
        street = this.street,
        buildingNumber = this.buildingNumber,
        coordinate = this.coordinates.toCoordinates()
    )
}