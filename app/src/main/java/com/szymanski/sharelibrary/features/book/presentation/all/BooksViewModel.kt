package com.szymanski.sharelibrary.features.book.presentation.all

import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.features.book.domain.usecase.SaveBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator

class BooksViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val saveBookUseCase: SaveBookUseCase,
) : BaseViewModel(errorMapper) {


    fun openSaveBookScreen() {
        bookNavigator.openSaveBookScreen()
    }
}