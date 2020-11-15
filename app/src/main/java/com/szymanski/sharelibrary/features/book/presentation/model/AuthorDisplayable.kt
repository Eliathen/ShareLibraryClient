package com.szymanski.sharelibrary.features.book.presentation.model

import com.szymanski.sharelibrary.features.book.domain.model.Author
import java.io.Serializable

class AuthorDisplayable(
    val name: String?,
    val surname: String?,
) : Serializable {

    constructor(author: Author) : this(
        name = author.name,
        surname = author.surname
    )

    fun toAuthor() = Author(
        name = this.name,
        surname = this.surname
    )
}
