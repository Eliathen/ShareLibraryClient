package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.book.domain.BookRepository
import com.szymanski.sharelibrary.features.book.domain.model.Book

class GetUsersBookUseCase(
    private val bookRepository: BookRepository,
) : BaseUseCase<List<Book>, Long>() {
    override suspend fun action(params: Long): List<Book> {
        return bookRepository.getUsersBook(params)
    }
}
