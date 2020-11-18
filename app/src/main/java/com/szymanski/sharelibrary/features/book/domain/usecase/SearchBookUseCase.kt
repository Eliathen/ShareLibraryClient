package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.book.domain.BookRepository
import com.szymanski.sharelibrary.features.book.domain.model.Book

class SearchBookUseCase(
    private val bookRepository: BookRepository,
) : BaseUseCase<List<Book>, String>() {
    override suspend fun action(params: String): List<Book> {
        return bookRepository.searchBooks(params)
    }

}
