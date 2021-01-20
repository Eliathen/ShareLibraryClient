package com.szymanski.sharelibrary.core.api.model.request

import com.szymanski.sharelibrary.features.book.domain.model.Language

data class LanguageRequest(
    val id: Int,
    val name: String,
) {
    constructor(language: Language) : this(
        id = language.id!!,
        name = language.name!!
    )
}
