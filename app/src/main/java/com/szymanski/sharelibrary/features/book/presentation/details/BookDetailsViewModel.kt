package com.szymanski.sharelibrary.features.book.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.usecase.FinishExchangeByBookIdUseCase
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetUserExchangesUseCase
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable

class BookDetailsViewModel(
    private val bookNavigator: BookNavigator,
    private val finishExchangeByBookIdUseCase: FinishExchangeByBookIdUseCase,
    private val userStorage: UserStorage,
    private val getUserExchangesUseCase: GetUserExchangesUseCase,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {


    private val _book: MutableLiveData<Book> by lazy {
        MutableLiveData()
    }

    val book: LiveData<BookDisplayable> by lazy {
        _book.map {
            BookDisplayable(it)
        }
    }

    private val _bookStatus: MutableLiveData<BookStatus> by lazy {
        MutableLiveData()
    }

    val bookStatus: LiveData<BookStatus> by lazy {
        _bookStatus
    }

    fun setBook(bookDisplayable: BookDisplayable) {
        _book.value = bookDisplayable.toBook()
        _bookStatus.value = bookDisplayable.status
    }

    fun finishExchange(bookId: Long) {
        finishExchangeByBookIdUseCase(
            scope = viewModelScope,
            params = bookId
        ) { result ->
            result.onSuccess {
                val book = _book.value!!
                book.status = BookStatus.AT_OWNER
                _book.value = book
                _bookStatus.value = book.status
            }
            result.onFailure {
                if (it is NoSuchElementException) {
                    showMessage("Only person which created exchange can finish it")
                } else {
                    handleFailure(it)
                }
            }
        }
    }

    fun displayUserProfile() {
        bookNavigator.openOtherUserProfileScreen(_book.value?.atUser?.id!!)
    }

    fun displayExchangeOnMap() {
        getUserExchangesUseCase(
            scope = viewModelScope,
            params = userStorage.getUserId()
        ) { result ->
            result.onSuccess { exchanges ->
                val exchange =
                    exchanges.first { it.book.id == _book.value?.id }
                        .also {
                            it.book.cover = book.value?.cover
                        }
                bookNavigator.displayExchangeOnMap(ExchangeDisplayable(exchange))
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

}