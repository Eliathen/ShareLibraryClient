package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.features.home.domain.RequirementRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetRequirementByExchangeIdUseCaseTest {
    @Test
    fun `when use case is invoked than execute getRequirementsByExchangeId method from repository`() {
        //given
        val repository = mockk<RequirementRepository>(relaxed = true)
        val useCase = GetRequirementByExchangeIdUseCase(repository)
        val exchangeId = 1L
        //when
        useCase(
            scope = GlobalScope,
            params = exchangeId
        )
        //then
        coVerify { repository.getRequirementsByExchangeId(exchangeId) }

    }
}