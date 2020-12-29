package com.szymanski.sharelibrary.core.api.model.request

class WithdrawBookRequest(
    val userId: Long,
    val bookId: Long,
) {
    companion object
}
