package com.szymanski.sharelibrary.features.book.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.usecase.FinishExchangeUseCase

class BookDetailsViewModel(
    private val bookNavigator: BookNavigator,
    errorMapper: ErrorMapper,
    private val finishExchangeUseCase: FinishExchangeUseCase,
) : BaseViewModel(errorMapper) {

    private val _book: MutableLiveData<BookDisplayable> by lazy {
        MutableLiveData()
    }

    val book: LiveData<BookDisplayable> by lazy {
        _book
    }

    private val _bookStatus: MutableLiveData<BookStatus> by lazy {
        MutableLiveData()
    }

    val bookStatus: LiveData<BookStatus> by lazy {
        _bookStatus
    }

    fun setBook(bookDisplayable: BookDisplayable) {
        _book.value = bookDisplayable
        _bookStatus.value = bookDisplayable.status
    }

    fun finishExchange(bookId: Long) {
        finishExchangeUseCase(
            scope = viewModelScope,
            params = bookId
        ) { result ->
            result.onSuccess {
                val book = _book.value!!
                book.status = BookStatus.AT_OWNER
                _book.value = book
                _bookStatus.value = book.status
            }
            result.onFailure { handleFailure(it) }
        }
    }

    fun goBackToBookScreen() {
        bookNavigator.returnFromBookDetailsScreen()
    }

}