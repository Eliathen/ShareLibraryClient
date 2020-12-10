package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository

class FinishExchangeUseCase(
    private val exchangeRepository: ExchangeRepository,
    private val userStorage: UserStorage,
) : BaseUseCase<Unit, Long>() {
    private val TAG = "FinishExchangeUseCase"
    override suspend fun action(params: Long) {
        val exchanges = exchangeRepository.getUserExchanges(userStorage.getUserId())
        val id = exchanges.first { exchange -> exchange.book.id == params }.id
        exchangeRepository.finishExchange(id)
    }
}