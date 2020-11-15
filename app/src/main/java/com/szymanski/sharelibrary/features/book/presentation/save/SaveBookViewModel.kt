package com.szymanski.sharelibrary.features.book.presentation.save

import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.features.book.domain.usecase.SaveBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.all.model.BookDisplayable

class SaveBookViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val saveBookUseCase: SaveBookUseCase,
) : BaseViewModel(errorMapper) {


    fun saveBook(book: BookDisplayable) {
        val saveBook = book.toBook()
        saveBookUseCase(
            scope = viewModelScope,
            params = saveBook
        ) {
            it.onSuccess {
                //TODO what do else
                bookNavigator.openBooksScreen()
            }
            it.onFailure { throwable ->
                handleFailure(throwable)
            }
        }
    }
}