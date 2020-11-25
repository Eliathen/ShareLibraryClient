package com.szymanski.sharelibrary.features.book.presentation.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.api.model.request.WithdrawBookRequest
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.local.UserStorage
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.GetUsersBookUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.WithdrawBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable

class BooksViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val getCoverUseCase: GetCoverUseCase,
    private val getUsersBookUseCase: GetUsersBookUseCase,
    private val withdrawBookUseCase: WithdrawBookUseCase,
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
}