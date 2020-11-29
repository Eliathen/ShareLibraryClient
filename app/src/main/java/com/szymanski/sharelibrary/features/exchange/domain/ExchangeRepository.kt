package com.szymanski.sharelibrary.features.exchange.domain

import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange

interface ExchangeRepository {

    suspend fun shareBook(exchange: Exchange): Exchange
    suspend fun getExchanges(userId: Long): List<Exchange>

}