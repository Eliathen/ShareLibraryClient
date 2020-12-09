package com.szymanski.sharelibrary.core.api.model.request


data class ExecuteExchangeRequest(
    private val exchangeId: Long?,
    private val withUserId: Long?,
    private val forBookId: Long?,
) {

    constructor(params: Map<String, Long>) : this(
        exchangeId = params[EXCHANGE_ID_KEY],
        withUserId = params[WITH_USER_ID_KEY],
        forBookId = if (params.containsKey(FOR_BOOK_ID_KEY)) params[FOR_BOOK_ID_KEY] else null
    )

    companion object {
        const val EXCHANGE_ID_KEY = "exchangeId"
        const val WITH_USER_ID_KEY = "withUserId"
        const val FOR_BOOK_ID_KEY = "forBookId"

    }
}