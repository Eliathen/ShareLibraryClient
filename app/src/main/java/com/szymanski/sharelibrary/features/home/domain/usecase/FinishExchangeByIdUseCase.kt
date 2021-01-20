package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository

class FinishExchangeByIdUseCase(private val exchangeRepository: ExchangeRepository) :
    BaseUseCase<Unit, Long>() {
    override suspend fun action(params: Long) {
        exchangeRepository.finishExchange(params)
    }

}
