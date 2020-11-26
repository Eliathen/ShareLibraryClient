package com.szymanski.sharelibrary.features.exchange.domain.model

import com.szymanski.sharelibrary.core.utils.ExchangeType
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.User


data class Exchange(
    val book: Book,
    val coordinates: Coordinate,
    val deposit: Double,
    val exchangeType: ExchangeType,
    val id: Long?,
    val isFinished: Boolean,
    val user: User,
)
