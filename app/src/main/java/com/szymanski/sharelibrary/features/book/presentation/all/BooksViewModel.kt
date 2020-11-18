package com.szymanski.sharelibrary.features.book.presentation.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.local.UserStorage
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetUsersBookUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.SaveBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable

class BooksViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val saveBookUseCase: SaveBookUseCase,
    private val getUsersBookUseCase: GetUsersBookUseCase,
    private val userStorage: UserStorage,
) : BaseViewModel(errorMapper) {

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
        getUsersBookUseCase(scope = viewModelScope, params = userId) {
            setIdleState()
            it.onSuccess { books -> _books.value = books }

            it.onFailure { throwable -> handleFailure(throwable) }
        }
    }


    fun openSearchBookScreen() {
        bookNavigator.openSearchBookScreen()
    }
}