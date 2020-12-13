package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange

class ExecuteExchangeUseCase(
    private val exchangeRepository: ExchangeRepository,
) : BaseUseCase<Exchange, Map<String, Long>>() {

    override suspend fun action(params: Map<String, Long>): Exchange {
        return exchangeRepository.executeExchange(params)
    }
}
