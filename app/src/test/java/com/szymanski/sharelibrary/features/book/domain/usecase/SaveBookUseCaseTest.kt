package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.book.domain.BookRepository
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class SaveBookUseCaseTest {

    @Test
    fun `when use case is invoked than execute saveBook from repository`() {
        //given
        val repository = mockk<BookRepository>(relaxed = true)
        val userId = 1L
        val storage = mockk<UserStorage> {
            coEvery { getUserId() } returns userId
        }
        val useCase = SaveBookUseCase(repository, storage)
        val book = Book.Companion.mock()
        //when
        useCase(
            scope = GlobalScope,
            params = book
        )
        //then
        coVerify { repository.saveBook(book, userId) }

    }
}