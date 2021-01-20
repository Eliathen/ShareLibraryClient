package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.core.api.model.request.ExecuteExchangeRequest
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class ExecuteExchangeUseCaseTest {
    @Test
    fun `when use case is invoked than execute executeExchange method from repository`() {
        //given
        val repository = mockk<ExchangeRepository>(relaxed = true)
        val useCase = ExecuteExchangeUseCase(repository)
        val params = mapOf(ExecuteExchangeRequest.EXCHANGE_ID_KEY to 1L,
            ExecuteExchangeRequest.WITH_USER_ID_KEY to 1L,
            ExecuteExchangeRequest.FOR_BOOK_ID_KEY to 1L)
        //when
        useCase(
            scope = GlobalScope,
            params = params
        )
        //then
        coVerify { repository.executeExchange(params) }

    }
}