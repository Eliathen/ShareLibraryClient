package com.szymanski.sharelibrary.features.book.presentation.searchbook

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.SearchBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable

class SearchBookViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val searchBookUseCase: SearchBookUseCase,
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


    fun searchBook(query: String) {
        setPendingState()
        searchBookUseCase(viewModelScope, params = query) {
            setIdleState()
            it.onSuccess { book ->
                _searchBooks.value = book
            }
            it.onFailure {

            }
        }
    }

    fun openSaveBookScreen() {
        Log.d("SearchBookViewModel", "XDDDD")
        bookNavigator.openSaveBookScreen()
    }

}
