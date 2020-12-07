package com.szymanski.sharelibrary.features.book.navigation

import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable

interface BookNavigator {

    fun openSaveBookScreen()

    fun openBooksScreen()

    fun openSearchBookScreen()

    fun openBookDetailsScreen(bookDisplayable: BookDisplayable)

    fun openOtherUserBookDetailsScreen(bookDisplayable: BookDisplayable)

    fun returnFromBookDetailsScreen()

}
