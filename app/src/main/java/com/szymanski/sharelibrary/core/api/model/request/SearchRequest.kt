package com.szymanski.sharelibrary.core.api.model.request

data class SearchRequest(
    val latitude: Double,
    val longitude: Double,
    val radius: Double?,
    val categories: List<String>?,
    val query: String?,
    val languageId: Int?,
    val conditions: List<Int>?,
) {
    companion object
}
