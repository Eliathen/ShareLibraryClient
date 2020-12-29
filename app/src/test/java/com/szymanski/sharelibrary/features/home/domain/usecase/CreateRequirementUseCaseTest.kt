package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.features.home.domain.RequirementRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class CreateRequirementUseCaseTest {
    @Test
    fun `when use case is invoked than execute createRequirement method from repository`() {
        //given
        val repository = mockk<RequirementRepository>(relaxed = true)
        val useCase = CreateRequirementUseCase(repository)
        val exchangeId = 1L
        val userId = 1L
        val params = Pair(exchangeId, userId)
        //when
        useCase(
            scope = GlobalScope,
            params = params
        )
        //then
        coVerify { repository.createRequirement(exchangeId, userId) }

    }
}