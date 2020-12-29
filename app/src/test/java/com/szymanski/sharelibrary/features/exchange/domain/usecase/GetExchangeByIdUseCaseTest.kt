package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetExchangeByIdUseCaseTest {

    @Test
    fun `when use case is invoked than execute getExchangeById method from repository`() {
        //given
        val repository = mockk<ExchangeRepository>(relaxed = true)
        val useCase = GetExchangeByIdUseCase(repository)
        //when
        val exchangeId = 1L
        useCase(
            params = exchangeId,
            scope = GlobalScope
        )
        //then
        coVerify { repository.getExchangeById(exchangeId) }
    }

}