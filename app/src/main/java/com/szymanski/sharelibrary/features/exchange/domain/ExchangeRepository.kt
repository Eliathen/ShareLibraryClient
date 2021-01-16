package com.szymanski.sharelibrary.features.exchange.domain

import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.home.domain.model.ExchangeDetails

interface ExchangeRepository {

    suspend fun shareBook(exchange: Exchange): Exchange
    suspend fun getNotUserExchanges(userId: Long): List<Exchange>
    suspend fun finishExchange(exchangeId: Long?)
    suspend fun getUserExchanges(userId: Long): List<Exchange>
    suspend fun getExchangeById(exchangeId: Long): Exchange
    suspend fun executeExchange(params: Map<String, Long>): Exchange
    suspend fun getExchangeByAtUserId(userId: Long): List<Exchange>
    suspend fun getExchangesByFilters(
        latitude: Double,
        longitude: Double,
        radius: Double,
        categories: List<String>?,
        query: String?,
        languageId: Int?,
        conditions: List<Int>?,
    ): List<Exchange>

    suspend fun getExchangesLinkedWithUser(userId: Long): List<ExchangeDetails>
}