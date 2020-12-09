package com.szymanski.sharelibrary.features.exchange.presentation.exchangedbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangesByAtUserId
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable

class ExchangedBookViewModel(
    errorMapper: ErrorMapper,
    private val getExchangesByAtUserId: GetExchangesByAtUserId,
    private val userStorage: UserStorage,
    private val getCoverUseCase: GetCoverUseCase,
) : BaseViewModel(errorMapper) {

    private val _exchanges: MutableLiveData<List<Exchange>> by lazy {
        MutableLiveData<List<Exchange>>().also {
            loadExchanges()
        }
    }
    val exchanges: LiveData<List<ExchangeDisplayable>> by lazy {
        _exchanges.map {
            it.map { exchange ->
                ExchangeDisplayable(exchange)
            }
        }
    }

    private val _book: MutableLiveData<Book> by lazy {
        MutableLiveData<Book>()
    }
    val book: LiveData<BookDisplayable> by lazy {
        _book.map {
            BookDisplayable(it)
        }
    }

    private fun loadExchanges() {
        setPendingState()
        getExchangesByAtUserId(
            scope = viewModelScope,
            params = userStorage.getUserId()
        ) { result ->
            setIdleState()
            result.onSuccess { _exchanges.value = it }
            result.onFailure { handleFailure(it) }
        }
    }

    fun downloadCover(bookDisplayable: BookDisplayable) {
        getCoverUseCase(
            scope = viewModelScope,
            params = bookDisplayable.id!!
        ) { result ->
            result.onSuccess {
                bookDisplayable.cover = it
                _book.value = bookDisplayable.toBook()
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }
}