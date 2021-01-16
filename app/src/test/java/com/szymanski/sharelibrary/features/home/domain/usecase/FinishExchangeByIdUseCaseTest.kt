package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class FinishExchangeByIdUseCaseTest {


    @Test
    fun `when use case is invoked than execute finish exchange method from repository`() {
        //given
        val repository = mockk<ExchangeRepository>(relaxed = true)
        val useCase = FinishExchangeByIdUseCase(repository)
        val exchangeId = 1L
        //when
        useCase(
            scope = GlobalScope,
            params = exchangeId
        )
        //then
        coVerify { repository.finishExchange(exchangeId) }

    }

}