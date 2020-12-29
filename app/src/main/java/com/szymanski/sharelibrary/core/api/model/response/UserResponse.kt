package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.user.domain.model.User


data class UserResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("coordinates") val coordinatesResponse: CoordinatesResponse,
    @SerializedName("books") val books: List<BookResponse>?,
) {
    fun toUser() = User(
        id = this.id,
        email = this.email,
        username = this.username,
        password = null,
        name = this.name,
        surname = this.surname,
        coordinates = this.coordinatesResponse.toCoordinates(),
        books = this.books?.map { book -> book.toBook() }
    )
}
