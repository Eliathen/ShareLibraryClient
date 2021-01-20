package com.szymanski.sharelibrary.core.api.model.request

data class AssignBookRequest(
    val userId: Long,
    val bookId: Long,
) {
    companion object
}
