package com.szymanski.sharelibrary.features.user.domain.usecase

import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.Login
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class LoginUserUseCaseTest {

    @Test
    fun `when use case is invoked than execute login method from repository`() {
        //given
        val repository = mockk<UserRepository>(relaxed = true)
        val useCase = LoginUserUseCase(repository)
        //when
        val login = Login.Companion.mock()
        useCase(
            params = login,
            scope = GlobalScope
        )
        //then
        coVerify { repository.login(login) }
    }

}