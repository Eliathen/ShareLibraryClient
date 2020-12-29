package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange

class GetExchangesByAtUserIdUseCase(
    private val exchangeRepository: ExchangeRepository,
) : BaseUseCase<List<Exchange>, Long>() {
    override suspend fun action(params: Long): List<Exchange> {
        return exchangeRepository.getExchangeByAtUserId(params)
    }
}