package com.szymanski.sharelibrary.core.api.model.request

import com.szymanski.sharelibrary.core.utils.ExchangeType
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange


data class SaveExchangeRequest(
    val bookId: Long,
    val coordinates: CoordinatesRequest,
    val deposit: Double,
    val exchangeType: ExchangeType,
    val userId: Long,
) {
    constructor(exchange: Exchange) : this(
        bookId = exchange.book.id!!,
        coordinates = CoordinatesRequest(exchange.coordinates),
        deposit = exchange.deposit,
        exchangeType = exchange.exchangeType,
        userId = exchange.user.id!!
    )
}
