package com.szymanski.sharelibrary.features.user.domain.usecase

import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class EditUserUseCaseTest {


    @Test
    fun `when use case is invoked than execute editUser method from repository`() {
        //given
        val repository = mockk<UserRepository>(relaxed = true)
        val useCase = EditUserUseCase(repository)
        val user = User.Companion.mock()
        //when
        useCase(
            params = user,
            scope = GlobalScope
        )
        //then
        coVerify { repository.editUser(user) }
    }
}