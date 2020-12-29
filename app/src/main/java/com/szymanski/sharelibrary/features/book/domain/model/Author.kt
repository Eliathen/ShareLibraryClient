package com.szymanski.sharelibrary.features.book.domain.model

import java.io.Serializable

data class Author(
    val id: Long?,
    val name: String?,
    val surname: String?,
) : Serializable {
    companion object
}
