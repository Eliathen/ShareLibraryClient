package com.szymanski.sharelibrary.features.exchange.domain

import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange

interface ExchangeRepository {

    suspend fun shareBook(exchange: Exchange): Exchange
    suspend fun getNotUserExchanges(userId: Long): List<Exchange>
    suspend fun finishExchange(exchangeId: Long?)
    suspend fun getUserExchanges(userId: Long): List<Exchange>
    suspend fun getExchangeById(exchangeId: Long): Exchange
    suspend fun executeExchange(params: Map<String, Long>): Exchange
    suspend fun getExchangeByAtUserId(userId: Long): List<Exchange>
}