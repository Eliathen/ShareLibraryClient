package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.book.domain.BookRepository
import com.szymanski.sharelibrary.features.book.domain.model.Language

class GetLanguagesUseCase(
    private val bookRepository: BookRepository,
) : BaseUseCase<List<Language>, Unit>() {
    override suspend fun action(params: Unit): List<Language> {
        return bookRepository.getLanguages()
    }
}
