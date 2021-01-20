package com.szymanski.sharelibrary.features.home.presentation.model

import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.home.domain.model.ExchangeDetails
import com.szymanski.sharelibrary.features.user.presentation.model.CoordinateDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

data class ExchangeDetailsDisplayable(
    val book: BookDisplayable,
    val coordinates: CoordinateDisplayable,
    val deposit: Double,
    val distance: Double?,
    val exchangeStatus: ExchangeStatus,
    val id: Long,
    val user: UserDisplayable,
    val withUser: UserDisplayable?,
    val forBook: BookDisplayable?,
) {
    constructor(exchangeDetails: ExchangeDetails) : this(
        BookDisplayable(exchangeDetails.book),
        CoordinateDisplayable(exchangeDetails.coordinates),
        exchangeDetails.deposit,
        exchangeDetails.distance,
        exchangeDetails.exchangeStatus,
        exchangeDetails.id,
        UserDisplayable(exchangeDetails.user),
        exchangeDetails.withUser?.let { UserDisplayable(it) },
        exchangeDetails.forBook?.let { BookDisplayable(it) }
    )
}