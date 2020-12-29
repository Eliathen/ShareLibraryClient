package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.features.home.domain.RequirementRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetUserRequirementsTest {
    @Test
    fun `when use case is invoked than execute getUserRequirements method from repository`() {
        //given
        val repository = mockk<RequirementRepository>(relaxed = true)
        val useCase = GetUserRequirements(repository)
        val userId = 1L
        //when
        useCase(
            scope = GlobalScope,
            params = userId
        )
        //then
        coVerify { repository.getUserRequirements(userId) }

    }
}