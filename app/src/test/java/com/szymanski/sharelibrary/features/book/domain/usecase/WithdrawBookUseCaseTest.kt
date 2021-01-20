package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.api.model.request.WithdrawBookRequest
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class WithdrawBookUseCaseTest {

    @Test
    fun `when use case is invoked than execute withDraw method from repository`() {
        //given
        val repository = mockk<UserRepository>(relaxed = true)
        val useCase = WithdrawBookUseCase(repository)
        val request = WithdrawBookRequest.Companion.mock()
        //when
        useCase(
            params = request,
            scope = GlobalScope
        )
        //then
        coVerify { repository.withdrawBook(request) }
    }
}
