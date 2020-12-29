package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange

class ShareBookUseCase(
    private val exchangeRepository: ExchangeRepository,
) : BaseUseCase<Exchange, Exchange>() {
    override suspend fun action(params: Exchange): Exchange {
        return exchangeRepository.shareBook(params)
    }
}