package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.core.api.model.request.SearchRequest
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetExchangesByFiltersUseCaseTest {

    @Test
    fun `when use case is invoked than execute getExchangesByFilters method from repository`() {
        //given

        val repository = mockk<ExchangeRepository>(relaxed = true)
        val useCase = GetExchangesByFiltersUseCase(repository)
        //when
        val searchRequest = SearchRequest.Companion.mock()
        val (
            latitude,
            longitude,
            radius,
            categories,
            query,
        ) = searchRequest
        useCase(
            params = searchRequest,
            scope = GlobalScope
        )
        //then
        coVerify {
            repository.getExchangesByFilters(
                latitude,
                longitude,
                radius!!,
                categories,
                query
            )
        }
    }
}