package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange

class GetExchangeByIdUseCase(
    private val exchangeRepository: ExchangeRepository,
) : BaseUseCase<Exchange, Long>() {

    override suspend fun action(params: Long): Exchange {
        return exchangeRepository.getExchangeById(params)
    }
}
