package com.szymanski.sharelibrary.features.home.domain.model

import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.User

data class ExchangeDetails(
    val book: Book,
    val coordinates: Coordinate,
    val deposit: Double,
    val distance: Double?,
    val exchangeStatus: ExchangeStatus,
    val id: Long,
    val user: User,
    val withUser: User?,
    val forBook: Book?,
) {

    companion object
}
