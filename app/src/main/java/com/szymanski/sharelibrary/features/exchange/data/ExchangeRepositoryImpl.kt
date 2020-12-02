package com.szymanski.sharelibrary.features.exchange.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.SaveExchangeRequest
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.exception.callOrThrow
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange

class ExchangeRepositoryImpl(
    private val api: Api,
    private val errorWrapper: ErrorWrapper,
) : ExchangeRepository {
    override suspend fun shareBook(exchange: Exchange): Exchange {
        return callOrThrow(errorWrapper) {
            api.saveExchange(SaveExchangeRequest(exchange)).toExchange()
        }
    }

    override suspend fun getExchanges(userId: Long): List<Exchange> {
        return callOrThrow(errorWrapper) {
            api.getExchanges(userId).map {
                it.toExchange()
            }
        }
    }
}