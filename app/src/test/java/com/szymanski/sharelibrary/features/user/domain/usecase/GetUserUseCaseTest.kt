package com.szymanski.sharelibrary.features.user.domain.usecase

import com.szymanski.sharelibrary.features.user.domain.UserRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetUserUseCaseTest {

    @Test
    fun `when use case is invoked than execute get user method from repository`() {
        //given
        val repository = mockk<UserRepository>(relaxed = true)
        val useCase = GetUserUseCase(repository)
        //when
        useCase(
            params = 1,
            scope = GlobalScope
        )
        //then
        coVerify { repository.getUser(1) }
    }
}