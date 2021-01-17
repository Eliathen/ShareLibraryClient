package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetExchangesUseCaseTest {


    @Test
    fun `when use case is invoked than execute get exchanges method from repository`() {
        //given
        val repository = mockk<ExchangeRepository>(relaxed = true)
        val useCase = GetExchangesUseCase(repository)
        val userId = 1L
        //when
        useCase(
            params = userId,
            scope = GlobalScope
        )
        //then
        coVerify { repository.getUserExchanges(userId) }
    }

}