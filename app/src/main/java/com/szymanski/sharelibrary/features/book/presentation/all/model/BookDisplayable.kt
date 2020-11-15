package com.szymanski.sharelibrary.features.book.presentation.all.model

import com.szymanski.sharelibrary.features.book.domain.model.Book

class BookDisplayable(
    val title: String?,
    val authorsDisplayable: List<AuthorDisplayable>?,
    val image: ByteArray?,
) {
    constructor(book: Book) : this(
        title = book.title,
        authorsDisplayable = book.authors?.map { AuthorDisplayable(it) },
        image = book.image
    )

    fun toBook() = Book(
        title = this.title,
        authors = this.authorsDisplayable?.map { it.toAuthor() },
        image = this.image
    )
}