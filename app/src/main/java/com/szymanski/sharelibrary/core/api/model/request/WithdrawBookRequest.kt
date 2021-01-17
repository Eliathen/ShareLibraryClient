package com.szymanski.sharelibrary.core.api.model.request

data class WithdrawBookRequest(
    val userId: Long,
    val bookId: Long,
) {
    companion object
}
