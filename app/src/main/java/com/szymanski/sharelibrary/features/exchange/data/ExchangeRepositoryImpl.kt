package com.szymanski.sharelibrary.features.exchange.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.ExecuteExchangeRequest
import com.szymanski.sharelibrary.core.api.model.request.SaveExchangeRequest
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.exception.callOrThrow
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange

class ExchangeRepositoryImpl(
    private val api: Api,
    private val errorWrapper: ErrorWrapper,
    private val userStorage: UserStorage,
) : ExchangeRepository {

    override suspend fun shareBook(exchange: Exchange): Exchange {
        return callOrThrow(errorWrapper) {
            api.saveExchange(SaveExchangeRequest(exchange)).toExchange()
        }
    }

    override suspend fun getNotUserExchanges(userId: Long): List<Exchange> {
        return callOrThrow(errorWrapper) {
            api.getExchangesByUserId(userId)
                .filter { it.exchangeStatus == ExchangeStatus.STARTED }
                .map {
                    it.toExchange()
                }
        }
    }

    override suspend fun finishExchange(exchangeId: Long?) {
        return callOrThrow(errorWrapper) {
            api.finishExchange(exchangeId)
        }
    }

    override suspend fun getUserExchanges(userId: Long): List<Exchange> {
        return callOrThrow(errorWrapper) {
            api.getExchangesByUserId(userId).map {
                it.toExchange()
            }
        }
    }

    override suspend fun getExchangeById(exchangeId: Long): Exchange {
        return callOrThrow(errorWrapper) {
            api.getExchangeById(exchangeId).toExchange()
        }
    }


    override suspend fun getExchangesByFilters(
        latitude: Double,
        longitude: Double,
        radius: Double,
        categories: List<String>?,
        query: String?,
        languageId: Int?,
        conditions: List<Int>?,
    ): List<Exchange> {
        val userId = userStorage.getUserId()
        return callOrThrow(errorWrapper) {
            api.getExchangesWithFilter(
                latitude,
                longitude,
                radius,
                categories,
                if (query.isNullOrEmpty()) null else query,
                languageId,
                conditions
            ).filter { it.user.id != userId }.map { it.toExchange() }
        }
    }

    override suspend fun executeExchange(params: Map<String, Long>): Exchange {
        val executeExchangeRequest = ExecuteExchangeRequest(params)
        return callOrThrow(errorWrapper) {
            api.executeExchange(executeExchangeRequest).toExchange()
        }
    }

    override suspend fun getExchangeByAtUserId(userId: Long): List<Exchange> {
        return callOrThrow(errorWrapper) {
            api.exchangesByAtUserId(userId).map { it.toExchange() }
        }
    }
}