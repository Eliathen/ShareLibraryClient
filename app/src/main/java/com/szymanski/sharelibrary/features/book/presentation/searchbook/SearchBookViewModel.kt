package com.szymanski.sharelibrary.features.book.presentation.searchbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.SearchBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable

class SearchBookViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val searchBookUseCase: SearchBookUseCase,
    private val getCoverUseCase: GetCoverUseCase,
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
            it.onSuccess { books ->
                _searchBooks.value = books
                downloadImage(books)
            }
            it.onFailure {

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
                    result.onFailure { throwable ->
                    }
                }
            }
        }

    }
}
