package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.features.book.domain.BookRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class SearchBookUseCaseTest {

    @Test
    fun `when use case is invoked than execute searchBook from repository`() {
        //given
        val repository = mockk<BookRepository>(relaxed = true)
        val useCase = SearchBookUseCase(repository)
        val query = "query"
        //when
        useCase(
            scope = GlobalScope,
            params = query
        )
        //then
        coVerify { repository.searchBooks(query) }

    }
}