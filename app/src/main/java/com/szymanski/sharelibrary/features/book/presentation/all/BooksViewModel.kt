package com.szymanski.sharelibrary.features.book.presentation.all

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.api.model.request.WithdrawBookRequest
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.utils.ExchangeType
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.GetUsersBookUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.WithdrawBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.ShareBookUseCase
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase

class BooksViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val getCoverUseCase: GetCoverUseCase,
    private val getUsersBookUseCase: GetUsersBookUseCase,
    private val withdrawBookUseCase: WithdrawBookUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val shareBookUseCase: ShareBookUseCase,
    private val userStorage: UserStorage,
) : BaseViewModel(errorMapper) {

    private val TAG = "BooksViewModel"

    private val _books by lazy {
        MutableLiveData<List<Book>>()
            .also { getUsersBook(userStorage.getUserId()) }
    }

    val books by lazy {
        _books.map {
            it.map { book ->
                BookDisplayable(book)
            }
        }
    }

    private fun getUsersBook(userId: Long) {
        setPendingState()
        getUsersBookUseCase(scope = viewModelScope, params = userId) { result ->
            setIdleState()
            result.onSuccess { books ->
                _books.value = books
                downloadImage(books)
            }

            result.onFailure { throwable ->
                handleFailure(throwable)
            }
        }
    }


    fun openSearchBookScreen() {
        bookNavigator.openSearchBookScreen()
    }

    private fun downloadImage(books: List<Book>) {
        books.forEach { book ->
            book.id?.let {
                getCoverUseCase(viewModelScope, params = it) { result ->
                    result.onSuccess { cover ->
                        val booksWithCovers = mutableSetOf<Book>()
                        book.cover = cover
                        booksWithCovers.add(book)
                        _books.value?.let { it1 -> booksWithCovers.addAll(it1) }
                        _books.value = booksWithCovers.sortedBy { book -> book.id }.toList()
                    }
                    result.onFailure { _ ->
                    }
                }
            }
        }

    }

    fun withdrawBook(bookDisplayable: BookDisplayable?) {
        withdrawBookUseCase(
            scope = viewModelScope,
            params = WithdrawBookRequest(userStorage.getUserId(), bookDisplayable?.id!!)
        ) { result ->
            result.onSuccess { _books.value = it.books }
        }
    }

    fun shareBook(bookDisplayable: BookDisplayable?) {
        var user: User
        getUserUseCase(
            scope = viewModelScope,
            params = userStorage.getUserId()
        ) { result ->
            result.onSuccess {
                bookDisplayable?.toBook()?.let { it1 -> createExchange(it, it1) }
            }
            result.onFailure {
                handleFailure(Throwable())
            }
        }
    }

    private fun createExchange(user: User, book: Book) {
        val exchange = Exchange(
            id = null,
            book = book,
            user = user,
            deposit = 0.0,
            exchangeType = ExchangeType.FOR_BOOK,
            isFinished = false,
            coordinates = user.coordinates!!,
        )
        shareBookUseCase(
            scope = viewModelScope,
            params = exchange
        ) { result ->
            result.onSuccess {
                Log.d("BooksViewModel", it.toString())
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }
}