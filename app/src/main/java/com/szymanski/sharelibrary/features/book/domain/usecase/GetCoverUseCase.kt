package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.book.domain.BookRepository

class GetCoverUseCase(
    private val bookRepository: BookRepository,
) : BaseUseCase<ByteArray, Long>() {
    override suspend fun action(params: Long): ByteArray {
        return bookRepository.getCoverByBookId(params)
    }
}