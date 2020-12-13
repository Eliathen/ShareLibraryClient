package com.szymanski.sharelibrary.features.book.presentation.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.features.book.domain.usecase.SaveBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable

class SaveBookViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val saveBookUseCase: SaveBookUseCase,
) : BaseViewModel(errorMapper) {

    private val _book: MutableLiveData<BookDisplayable> by lazy {
        MutableLiveData<BookDisplayable>()
    }
    val book: LiveData<BookDisplayable> by lazy {
        _book
    }

    fun saveBook(book: BookDisplayable) {
        setPendingState()
        val saveBook = book.toBook()
        saveBookUseCase(
            scope = viewModelScope,
            params = saveBook
        ) {
            setIdleState()
            it.onSuccess {
                bookNavigator.openBooksScreen()
            }
            it.onFailure { throwable ->
                handleFailure(throwable)
            }
        }
    }

}