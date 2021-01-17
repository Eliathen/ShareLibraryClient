package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.FinishExchangeByBookIdUseCase
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetCurrentExchangesLinkedWithUserUseCaseTest {


    @Test
    fun `when use case is invoked than execute finish exchange method from repository`() {
        //given
        val userId = 1L
        val exchange = Exchange.mock()
        val repository = mockk<ExchangeRepository> {
            coEvery { getUserExchanges(userId) } returns listOf(exchange)
        }
        val userStorage = mockk<UserStorage> {
            every { getUserId() } returns userId
        }
        val useCase = FinishExchangeByBookIdUseCase(repository, userStorage)
        val bookId = 1L
        //when
        useCase(
            params = bookId,
            scope = GlobalScope,
            executeDispatcher = Dispatchers.IO
        )
        //then
        coVerify { repository.finishExchange(exchange.id) }
    }

}