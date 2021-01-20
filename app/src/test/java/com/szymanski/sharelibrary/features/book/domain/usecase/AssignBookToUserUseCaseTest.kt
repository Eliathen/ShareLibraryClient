package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.api.model.request.AssignBookRequest
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class AssignBookToUserUseCaseTest {


    @Test
    fun `when use case is invoked than execute assignBook method from repository`() {
        //given
        val repository = mockk<UserRepository>(relaxed = true)
        val useCase = AssignBookToUserUseCase(repository)
        val request = AssignBookRequest.Companion.mock()
        //when
        useCase(
            params = request,
            scope = GlobalScope
        )
        //then
        coVerify { repository.assignBook(request) }
    }
}