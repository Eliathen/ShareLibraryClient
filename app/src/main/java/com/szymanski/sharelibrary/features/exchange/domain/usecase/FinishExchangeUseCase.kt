package com.szymanski.sharelibrary.features.exchange.domain.usecase

import android.util.Log
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
        Log.d(TAG, "action: $exchanges")
        val id = exchanges.first { exchange -> exchange.book.id == params }.id
        Log.d(TAG, "action: $id")
        exchangeRepository.finishExchange(id)
    }
}