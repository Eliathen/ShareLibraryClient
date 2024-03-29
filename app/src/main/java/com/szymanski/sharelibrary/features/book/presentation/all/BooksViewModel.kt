package com.szymanski.sharelibrary.features.book.presentation.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.api.model.request.WithdrawBookRequest
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.GetUsersBookUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.WithdrawBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.FinishExchangeByBookIdUseCase
import com.szymanski.sharelibrary.features.exchange.domain.usecase.ShareBookUseCase
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase
import com.szymanski.sharelibrary.features.user.presentation.model.CoordinateDisplayable

class BooksViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val getCoverUseCase: GetCoverUseCase,
    private val getUsersBookUseCase: GetUsersBookUseCase,
    private val withdrawBookUseCase: WithdrawBookUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val shareBookUseCase: ShareBookUseCase,
    private val finishExchangeByBookIdUseCase: FinishExchangeByBookIdUseCase,
    private val userStorage: UserStorage,
) : BaseViewModel(errorMapper) {


    private val _books by lazy {
        MutableLiveData<List<Book>>()
    }

    val books by lazy {
        _books.map {
            it.map { book ->
                BookDisplayable(book)
            }
        }
    }

    private fun getUsersBook() {
        val userId = userStorage.getUserId()
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

    fun refreshBooks() {
        getUsersBook()
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
                    result.onFailure {
                        handleFailure(it)

                    }
                }
            }
        }
    }

    fun openSearchBookScreen() {
        bookNavigator.openSearchBookScreen()
    }

    fun withdrawBook(bookDisplayable: BookDisplayable?) {
        setPendingState()
        withdrawBookUseCase(
            scope = viewModelScope,
            params = WithdrawBookRequest(userStorage.getUserId(), bookDisplayable?.id!!)
        ) { result ->
            setIdleState()
            result.onSuccess { _books.value = it.books }
        }
    }

    fun shareBook(
        bookDisplayable: BookDisplayable,
        deposit: Double,
        coordinateDisplayable: CoordinateDisplayable,
    ) {
        getUserUseCase(
            scope = viewModelScope,
            params = userStorage.getUserId()
        ) { result ->
            result.onSuccess { user ->
                createExchange(user,
                    bookDisplayable.toBook(),
                    coordinateDisplayable.toCoordinate(),
                    deposit)
            }
            result.onFailure {
                handleFailure(Throwable())
            }
        }
    }

    fun shareBook(bookDisplayable: BookDisplayable, deposit: Double) {
        getUserUseCase(
            scope = viewModelScope,
            params = userStorage.getUserId()
        ) { result ->
            result.onSuccess { user ->
                createExchange(user, bookDisplayable.toBook(), user.coordinates!!, deposit)
            }
            result.onFailure {
                handleFailure(Throwable())
            }
        }
    }


    private fun createExchange(user: User, book: Book, coordinate: Coordinate, deposit: Double) {
        val exchange = Exchange(
            id = null,
            book = book,
            user = user,
            deposit = deposit,
            exchangeStatus = ExchangeStatus.STARTED,
            coordinates = coordinate,
            distance = null,
        )

        shareBookUseCase(
            scope = viewModelScope,
            params = exchange
        ) { result ->
            result.onSuccess { exchange ->
                val newBooks = _books.value!!.toMutableList()
                newBooks.forEachIndexed { index, book ->
                    if (book.id == exchange.book.id) {
                        exchange.book.cover = book.cover
                        newBooks[index] = exchange.book
                    }
                }
                _books.value = newBooks
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun openBookDetailsScreen(bookDisplayable: BookDisplayable) {
        bookNavigator.openBookDetailsScreen(bookDisplayable)
    }

    fun finishExchange(booksDisplayable: BookDisplayable) {
        setPendingState()
        booksDisplayable.id?.let { bookId ->
            finishExchangeByBookIdUseCase(
                scope = viewModelScope,
                params = bookId
            ) { result ->
                setIdleState()
                result.onSuccess {
                    val book = _books.value!!.first { it.id == booksDisplayable.id }
                    val newBooks = _books.value!!
                    newBooks.forEach {
                        if (it.id == book.id) {
                            book.status = BookStatus.AT_OWNER
                        }
                    }
                    _books.value = newBooks
                }
                result.onFailure { throwable ->
                    handleFailure(throwable)
                }
            }
        }
    }
}