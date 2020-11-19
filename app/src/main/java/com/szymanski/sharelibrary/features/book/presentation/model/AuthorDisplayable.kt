package com.szymanski.sharelibrary.features.book.presentation.model

import com.szymanski.sharelibrary.features.book.domain.model.Author
import java.io.Serializable

class AuthorDisplayable(
    val id: Long? = null,
    var name: String?,
    var surname: String?,
) : Serializable {

    constructor(author: Author) : this(
        id = author.id,
        name = author.name,
        surname = author.surname
    )

    fun toAuthor() = Author(
        id = this.id,
        name = this.name,
        surname = this.surname
    )
}
