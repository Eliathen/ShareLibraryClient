package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.core.api.model.request.SearchRequest
import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange

class GetExchangesByFiltersUseCase(
    private val exchangeRepository: ExchangeRepository,
) : BaseUseCase<List<Exchange>, SearchRequest>() {
    override suspend fun action(params: SearchRequest): List<Exchange> {
        return exchangeRepository.getExchangesByFilters(
            params.latitude,
            params.longitude,
            params.radius!!,
            params.categories,
            params.query,
            params.languageId,
            params.conditions
        )
    }

}
