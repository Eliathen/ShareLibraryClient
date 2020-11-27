package com.szymanski.sharelibrary.core.api.model.request

import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange


data class SaveExchangeRequest(
    val bookId: Long,
    val coordinates: CoordinatesRequest,
    val deposit: Double,
    val exchangeStatus: ExchangeStatus,
    val userId: Long,
) {
    constructor(exchange: Exchange) : this(
        bookId = exchange.book.id!!,
        coordinates = CoordinatesRequest(exchange.coordinates),
        deposit = exchange.deposit,
        exchangeStatus = exchange.exchangeStatus,
        userId = exchange.user.id!!
    )
}
