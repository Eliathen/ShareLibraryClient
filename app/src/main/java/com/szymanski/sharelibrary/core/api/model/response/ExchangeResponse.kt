package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.core.utils.ExchangeType
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange


data class ExchangeResponse(
    @SerializedName("book") val book: BookResponse,
    @SerializedName("coordinates") val coordinates: CoordinatesResponse,
    @SerializedName("deposit") val deposit: Double,
    @SerializedName("exchangeType") val exchangeType: ExchangeType,
    @SerializedName("id") val id: Long,
    @SerializedName("isFinished") val isFinished: Boolean,
    @SerializedName("user") val user: UserResponse,
) {
    fun toExchange() = Exchange(
        book = this.book.toBook(),
        coordinates = this.coordinates.toCoordinates(),
        deposit = this.deposit,
        exchangeType = this.exchangeType,
        id = this.id,
        isFinished = this.isFinished,
        user = this.user.toUser()
    )
}
