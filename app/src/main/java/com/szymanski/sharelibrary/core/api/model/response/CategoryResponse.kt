package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.book.domain.model.Category

data class CategoryResponse(
    @SerializedName("id") private val id: Int,
    @SerializedName("name") private val name: String,
) {
    fun toCategory() = Category(
        id = id,
        name = name
    )
}