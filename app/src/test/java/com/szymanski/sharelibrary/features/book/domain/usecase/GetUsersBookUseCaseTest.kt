package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.features.book.domain.BookRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetUsersBookUseCaseTest {

    @Test
    fun `when use case is invoked than execute getUsersBook method from repository`() {
        //given
        val repository = mockk<BookRepository>(relaxed = true)
        val useCase = GetUsersBookUseCase(repository)
        val userId = 1L
        //when
        useCase(
            params = userId,
            scope = GlobalScope
        )
        //then
        coVerify { repository.getUsersBook(userId) }
    }
}