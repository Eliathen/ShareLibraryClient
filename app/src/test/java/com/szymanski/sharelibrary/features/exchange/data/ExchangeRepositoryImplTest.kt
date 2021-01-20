package com.szymanski.sharelibrary.features.exchange.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.ExecuteExchangeRequest
import com.szymanski.sharelibrary.core.api.model.request.SaveExchangeRequest
import com.szymanski.sharelibrary.core.api.model.request.SearchRequest
import com.szymanski.sharelibrary.core.api.model.response.ExchangeResponse
import com.szymanski.sharelibrary.core.api.model.response.ExchangeWithDetailsResponse
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import retrofit2.Response

internal class ExchangeRepositoryImplTest {


    @Test
    fun `WHEN request share book THEN call save exchange from API `() {
        val request = SaveExchangeRequest.Companion.mock()
        val exchange = Exchange.mock()
        val api = mockk<Api> {
            coEvery { saveExchange(request) } returns ExchangeResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val userStorage = mockk<UserStorage> {
            every { getUserId() } returns 1L
        }
        val repository = ExchangeRepositoryImpl(api, errorWrapper, userStorage)
        //when
        runBlocking {
            repository.shareBook(exchange)
        }
        //then
        coVerify {
            api.saveExchange(request)
        }
    }

    @Test
    fun `WHEN request get User Exchanges THEN call get exchanges by user id method from API `() {
        val userId = 1L
        val api = mockk<Api> {
            coEvery { getExchangesByUserId(userId) } returns listOf(ExchangeResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val userStorage = mockk<UserStorage> {
            every { getUserId() } returns 1L
        }
        val repository = ExchangeRepositoryImpl(api, errorWrapper, userStorage)
        //when
        runBlocking {
            repository.getUserExchanges(userId)
        }
        //then
        coVerify {
            api.getExchangesByUserId(userId)
        }
    }

    @Test
    fun `WHEN request finish exchange THEN call finish exchange method from API `() {
        val exchangeId = 1L
        val api = mockk<Api> {
            coEvery { finishExchange(exchangeId) } returns Response.success(Unit)
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val userStorage = mockk<UserStorage> {
            every { getUserId() } returns 1L
        }
        val repository = ExchangeRepositoryImpl(api, errorWrapper, userStorage)
        //when
        runBlocking {
            repository.finishExchange(exchangeId)
        }
        //then
        coVerify {
            api.finishExchange(exchangeId)
        }
    }

    @Test
    fun `WHEN request get exchange by id THEN call get exchange by id method from API `() {
        val exchangeId = 1L
        val api = mockk<Api> {
            coEvery { getExchangeById(exchangeId) } returns ExchangeResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val userStorage = mockk<UserStorage> {
            every { getUserId() } returns 1L
        }
        val repository = ExchangeRepositoryImpl(api, errorWrapper, userStorage)
        //when
        runBlocking {
            repository.getExchangeById(exchangeId)
        }
        //then
        coVerify {
            api.getExchangeById(exchangeId)
        }
    }

    @Test
    fun `WHEN request get exchanges linked with user THEN call get exchanges linked with user method from API `() {
        val userId = 1L
        val api = mockk<Api> {
            coEvery { getExchangesLinkedWithUser(userId) } returns listOf(
                ExchangeWithDetailsResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val userStorage = mockk<UserStorage> {
            every { getUserId() } returns 1L
        }
        val repository = ExchangeRepositoryImpl(api, errorWrapper, userStorage)
        //when
        runBlocking {
            repository.getExchangesLinkedWithUser(userId)
        }
        //then
        coVerify {
            api.getExchangesLinkedWithUser(userId)
        }
    }

    @Test
    fun `WHEN request get exchanges by filters THEN call get exchanges with filters method from API `() {
        val userId = 1L
        val request = SearchRequest.mock()
        val api = mockk<Api> {
            coEvery {
                getExchangesWithFilter(
                    any(), any(), any(), any(), any(), any(), any()
                )
            } returns listOf(ExchangeResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val userStorage = mockk<UserStorage> {
            every { getUserId() } returns 1L
        }
        val repository = ExchangeRepositoryImpl(api, errorWrapper, userStorage)
        //when
        runBlocking {
            repository.getExchangesByFilters(
                request.latitude,
                request.longitude,
                request.radius!!,
                request.categories,
                request.query,
                request.languageId,
                request.conditions
            )
        }
        //then
        coVerify {
            api.getExchangesWithFilter(
                request.latitude,
                request.longitude,
                request.radius!!,
                request.categories,
                request.query,
                request.languageId,
                request.conditions
            )
        }
    }

    @Test
    fun `WHEN request execute exchange THEN call execute exchange method from API `() {
        val params = mapOf<String, Long>(
            ExecuteExchangeRequest.EXCHANGE_ID_KEY to 1L,
            ExecuteExchangeRequest.FOR_BOOK_ID_KEY to 1L,
            ExecuteExchangeRequest.WITH_USER_ID_KEY to 2L
        )
        val request = ExecuteExchangeRequest(params)
        val userId = 1L
        val api = mockk<Api> {
            coEvery { executeExchange(request) } returns ExchangeResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val userStorage = mockk<UserStorage> {
            every { getUserId() } returns 1L
        }
        val repository = ExchangeRepositoryImpl(api, errorWrapper, userStorage)
        //when
        runBlocking {
            repository.executeExchange(params)
        }
        //then
        coVerify {
            api.executeExchange(request)
        }
    }

    @Test
    fun `WHEN request get exchange by atUser id THEN call exchanges by at user id method from API `() {
        val userId = 1L
        val api = mockk<Api> {
            coEvery { exchangesByAtUserId(userId) } returns listOf(ExchangeResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val userStorage = mockk<UserStorage> {
            every { getUserId() } returns 1L
        }
        val repository = ExchangeRepositoryImpl(api, errorWrapper, userStorage)
        //when
        runBlocking {
            repository.getExchangeByAtUserId(userId)
        }
        //then
        coVerify {
            api.exchangesByAtUserId(userId)
        }
    }

}