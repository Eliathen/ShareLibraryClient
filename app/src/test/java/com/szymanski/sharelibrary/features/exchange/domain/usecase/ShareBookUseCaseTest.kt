package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class ShareBookUseCaseTest {
    @Test
    fun `when use case is invoked than execute shareBook method from repository`() {
        //given
        val repository = mockk<ExchangeRepository>(relaxed = true)
        val useCase = ShareBookUseCase(repository)
        //when
        val exchange = Exchange.Companion.mock()
        useCase(
            params = exchange,
            scope = GlobalScope
        )
        //then
        coVerify { repository.shareBook(exchange) }
    }
}