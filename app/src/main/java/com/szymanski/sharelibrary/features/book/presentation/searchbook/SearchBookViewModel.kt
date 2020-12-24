package com.szymanski.sharelibrary.features.book.presentation.searchbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.api.model.request.AssignBookRequest
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.AssignBookToUserUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.GetUsersBookUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.SearchBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable

class SearchBookViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val getUsersBookUseCase: GetUsersBookUseCase,
    private val searchBookUseCase: SearchBookUseCase,
    private val getCoverUseCase: GetCoverUseCase,
    private val assignBookToUserUseCase: AssignBookToUserUseCase,
    private val userStorage: UserStorage,
) : BaseViewModel(errorMapper) {

    private val _searchBooks by lazy {
        MutableLiveData<List<Book>>()
    }
    val searchBooks by lazy {
        _searchBooks.map { books ->
            books.map {
                BookDisplayable(it)
            }
        }
    }

    private val _usersBooks by lazy {
        MutableLiveData<List<Book>>().also {
            getUsersBooks()
        }
    }

    private fun getUsersBooks() {
        getUsersBookUseCase(scope = viewModelScope,
            params = userStorage.getUserId()) { result ->
            result.onSuccess { _usersBooks.value = it }
        }
    }

    val usersBooks by lazy {
        _usersBooks.map { books ->
            books.map {
                BookDisplayable(it)
            }
        }
    }


    fun searchBook(query: String) {
        setPendingState()
        searchBookUseCase(viewModelScope, params = query) { result ->
            setIdleState()
            result.onSuccess { books ->
                _searchBooks.value = books
                downloadImage(books)
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun openSaveBookScreen() {
        bookNavigator.openSaveBookScreen()
    }

    private fun downloadImage(books: List<Book>) {
        books.forEach { book ->
            book.id?.let { id ->
                getCoverUseCase(viewModelScope, params = id) { result ->
                    result.onSuccess { cover ->
                        val booksWithCovers = mutableSetOf<Book>()
                        book.cover = cover
                        booksWithCovers.add(book)
                        _searchBooks.value?.let { it1 -> booksWithCovers.addAll(it1) }
                        _searchBooks.value = booksWithCovers.sortedBy { it.id }.toList()
                    }
                    result.onFailure { _ ->
                    }
                }
            }
        }

    }

    fun assignBook(bookDisplayable: BookDisplayable?) {
        setPendingState()
        assignBookToUserUseCase(
            scope = viewModelScope,
            params = AssignBookRequest(userStorage.getUserId(), bookDisplayable?.id!!)
        ) { result ->
            setIdleState()
            result.onSuccess {
                _usersBooks.value = it.books
            }
            result.onFailure {

            }
        }
    }
}
