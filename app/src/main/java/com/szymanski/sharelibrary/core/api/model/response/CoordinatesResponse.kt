package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName

data class CoordinatesResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
)