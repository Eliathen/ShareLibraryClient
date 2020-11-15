package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.book.domain.BookRepository
import com.szymanski.sharelibrary.features.book.domain.model.Book

class SaveBookUseCase(
    private val bookRepository: BookRepository,
) : BaseUseCase<Unit, Book>() {

    override suspend fun action(params: Book) {
        bookRepository.saveBook(params)
    }
}