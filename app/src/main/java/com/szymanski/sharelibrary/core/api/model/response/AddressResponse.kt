package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName

data class AddressResponse(
    @SerializedName("buildingNumber") val buildingNumber: String,
    @SerializedName("city") val city: String,
    @SerializedName("coordinates") val coordinates: CoordinatesResponse,
    @SerializedName("country") val country: String,
    @SerializedName("postalCode") val postalCode: String,
    @SerializedName("id") val id: Int,
    @SerializedName("street") val street: String,
)