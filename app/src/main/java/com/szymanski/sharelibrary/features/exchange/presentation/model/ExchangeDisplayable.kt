package com.szymanski.sharelibrary.features.exchange.presentation.model

import android.os.Parcelable
import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.user.presentation.model.CoordinateDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExchangeDisplayable(
    val book: BookDisplayable,
    val coordinates: CoordinateDisplayable,
    val deposit: Double,
    val distance: Double?,
    val exchangeStatus: ExchangeStatus,
    val id: Long?,
    val user: UserDisplayable,
) : Parcelable {
    constructor(exchange: Exchange) : this(
        book = BookDisplayable(exchange.book),
        coordinates = CoordinateDisplayable(exchange.coordinates),
        user = UserDisplayable(exchange.user),
        deposit = exchange.deposit,
        distance = exchange.distance,
        exchangeStatus = exchange.exchangeStatus,
        id = exchange.id
    )

    fun toExchange() = Exchange(
        book = book.toBook(),
        coordinates = coordinates.toCoordinate(),
        deposit = deposit,
        distance = distance,
        exchangeStatus = exchangeStatus,
        id = id,
        user = user.toUser()
    )

    companion object
}
