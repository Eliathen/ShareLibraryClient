package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.home.domain.model.ExchangeDetails


data class ExchangeWithDetailsResponse(
    @SerializedName("book") val book: BookResponse,
    @SerializedName("coordinates") val coordinates: CoordinatesResponse,
    @SerializedName("deposit") val deposit: Double,
    @SerializedName("distance") val distance: Double?,
    @SerializedName("exchangeStatus") val exchangeStatus: ExchangeStatus,
    @SerializedName("id") val id: Long,
    @SerializedName("user") val user: UserResponse,
    @SerializedName("withUser") val withUser: UserResponse?,
    @SerializedName("forBook") val forBook: BookResponse?,
) {
    fun toExchangeDetails() = ExchangeDetails(
        book = this.book.toBook(),
        coordinates = this.coordinates.toCoordinates(),
        deposit = this.deposit,
        distance = this.distance,
        exchangeStatus = this.exchangeStatus,
        id = this.id,
        user = this.user.toUser(),
        withUser = this.withUser?.toUser(),
        forBook = this.forBook?.toBook()
    )
}
