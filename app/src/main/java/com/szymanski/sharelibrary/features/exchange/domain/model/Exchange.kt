package com.szymanski.sharelibrary.features.exchange.domain.model

import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.User


data class Exchange(
    var book: Book,
    var coordinates: Coordinate,
    var deposit: Double,
    var distance: Double?,
    var exchangeStatus: ExchangeStatus,
    var id: Long?,
    var user: User,
) {
    companion object
}
