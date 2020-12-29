package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.features.book.domain.BookRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetCoverUseCaseTest {

    @Test
    fun `when use case is invoked than execute getCoverByBookId method from repository`() {
        //given
        val repository = mockk<BookRepository>(relaxed = true)
        val useCase = GetCoverUseCase(repository)
        val bookId = 1L
        //when
        useCase(
            params = bookId,
            scope = GlobalScope
        )
        //then
        coVerify { repository.getCoverByBookId(bookId) }
    }
}