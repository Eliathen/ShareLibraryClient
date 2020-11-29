package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.user.domain.model.User


data class RegisterResponse(
    @SerializedName("coordinates") val coordinatesResponse: CoordinatesResponse,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("username") val username: String,
) {
    fun toUser() = User(
        email = this.email,
        id = this.id,
        name = this.name,
        surname = this.surname,
        username = this.username,
        password = null,
        coordinates = coordinatesResponse.toCoordinates(),
        books = null
    )
}