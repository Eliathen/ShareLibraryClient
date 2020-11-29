package com.szymanski.sharelibrary.features.exchange.all.presentation.model

import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.user.registration.presentation.model.CoordinateDisplayable
import com.szymanski.sharelibrary.features.user.registration.presentation.model.UserDisplayable

data class ExchangeDisplayable(
    val book: BookDisplayable,
    val coordinates: CoordinateDisplayable,
    val deposit: Double,
    val exchangeStatus: ExchangeStatus,
    val id: Long?,
    val user: UserDisplayable,
) {
    constructor(exchange: Exchange) : this(
        book = BookDisplayable(exchange.book),
        coordinates = CoordinateDisplayable(exchange.coordinates),
        user = UserDisplayable(exchange.user),
        deposit = exchange.deposit,
        exchangeStatus = exchange.exchangeStatus,
        id = exchange.id,
    )
}
