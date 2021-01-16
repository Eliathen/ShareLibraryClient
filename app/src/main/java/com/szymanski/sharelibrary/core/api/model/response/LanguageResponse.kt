package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.book.domain.model.Language

data class LanguageResponse(
    @SerializedName("id") private val id: Int,
    @SerializedName("name") private val name: String,
) {

    fun toLanguage() = Language(
        id = id, name = name
    )
    companion object
}