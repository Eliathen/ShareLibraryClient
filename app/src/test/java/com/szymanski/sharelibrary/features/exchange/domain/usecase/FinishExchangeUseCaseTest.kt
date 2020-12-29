package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class FinishExchangeUseCaseTest {
    @Test
    fun `when use case is invoked than execute finishExchange method from repository`() {
        //given
        val repository = mockk<ExchangeRepository> {
            coEvery { getUserExchanges(2L) } returns listOf(Exchange.Companion.mock())
        }
        val useCase = GetExchangeByIdUseCase(repository)
        //when
        val bookId = 1L
        val exchangeId = 1L
        useCase(
            params = bookId,
            scope = GlobalScope
        )
        //then
        coVerify { repository.getExchangeById(exchangeId) }
    }
}