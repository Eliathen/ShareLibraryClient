package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.user.domain.model.User

class UserWithoutBooks(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("coordinates") val coordinates: CoordinatesResponse,

    ) {
    fun toUser() = User(
        id = this.id,
        email = this.email,
        username = this.username,
        password = null,
        name = this.name,
        surname = this.surname,
        coordinates = this.coordinates.toCoordinates(),
    )
}