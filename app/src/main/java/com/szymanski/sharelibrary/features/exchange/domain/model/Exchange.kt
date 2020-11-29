package com.szymanski.sharelibrary.features.exchange.domain.model

import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.User


data class Exchange(
    val book: Book,
    val coordinates: Coordinate,
    val deposit: Double,
    val exchangeStatus: ExchangeStatus,
    val id: Long?,
    val user: User,
)
