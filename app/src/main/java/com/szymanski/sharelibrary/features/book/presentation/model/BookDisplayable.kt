package com.szymanski.sharelibrary.features.book.presentation.model

import com.szymanski.sharelibrary.features.book.domain.model.Book

class BookDisplayable(
    val id: Long?,
    val title: String?,
    val authorsDisplayable: List<AuthorDisplayable>?,
    val coverId: Long? = null,
    var cover: ByteArray?,
) {
    constructor(book: Book) : this(
        id = book.id,
        title = book.title,
        authorsDisplayable = book.authors?.map { AuthorDisplayable(it) },
        cover = book.cover
    )

    fun toBook() = Book(
        id = this.id,
        title = this.title,
        authors = this.authorsDisplayable?.map { it.toAuthor() },
        cover = this.cover,
    )
}