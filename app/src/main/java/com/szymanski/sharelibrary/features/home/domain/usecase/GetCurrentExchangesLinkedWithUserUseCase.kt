package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.home.domain.model.ExchangeDetails

class GetCurrentExchangesLinkedWithUserUseCase(
    private val exchangeRepository: ExchangeRepository,
) : BaseUseCase<List<ExchangeDetails>, Long>() {
    override suspend fun action(params: Long): List<ExchangeDetails> {
        return exchangeRepository.getExchangesLinkedWithUser(params)
    }

}
