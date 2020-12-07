package com.szymanski.sharelibrary.features.exchange.navigation

interface ExchangeNavigation {

    fun openExchangeDetails(exchangeId: Long)

    fun openOtherUserScreen(userId: Long)
}