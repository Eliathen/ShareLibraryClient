package com.szymanski.sharelibrary.features.book.navigation

import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable

interface BookNavigator {

    fun openSaveBookScreen(bookDisplayable: BookDisplayable?)

    fun openBooksScreen()

    fun openSearchBookScreen()

    fun openBookDetailsScreen(bookDisplayable: BookDisplayable)

    fun openOtherUserBookDetailsScreen(bookDisplayable: BookDisplayable)

    fun openOtherUserProfileScreen(otherUserId: Long)

    fun displayExchangeOnMap(exchange: ExchangeDisplayable)

}
