package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book

data class BookResponse(
    @SerializedName("id") private val id: Long,
    @SerializedName("title") private val title: String,
    @SerializedName("authors") private val authors: List<AuthorResponse>,
    @SerializedName("status") private val status: BookStatus,
    @SerializedName("atUser") private val atUser: UserWithoutBooks?,
    @SerializedName("categories") private val categories: List<CategoryResponse>,
) {

    fun toBook() = Book(
        id = this.id,
        title = this.title,
        authors = this.authors.map { it.toAuthor() }.toList(),
        cover = byteArrayOf(),
        status = this.status,
        atUser = this.atUser?.toUser(),
        categories = this.categories.map { it.toCategory() }
    )
}
