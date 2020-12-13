package com.szymanski.sharelibrary.features.book.presentation.model

import android.os.Parcelable
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import kotlinx.android.parcel.Parcelize

@Parcelize
class BookDisplayable(
    val id: Long?,
    val title: String?,
    val authorsDisplayable: List<AuthorDisplayable>?,
    val coverId: Long? = null,
    var cover: ByteArray?,
    var status: BookStatus?,
    val atUserDisplayable: UserDisplayable?,
    val categoriesDisplayable: List<CategoryDisplayable>?,
) : Parcelable {
    constructor(book: Book) : this(
        id = book.id,
        title = book.title,
        authorsDisplayable = book.authors?.map { AuthorDisplayable(it) },
        cover = book.cover,
        status = book.status,
        atUserDisplayable = book.atUser?.let { UserDisplayable(it) },
        categoriesDisplayable = book.categories?.map { CategoryDisplayable(it) }
    )

    fun toBook() = Book(
        id = this.id,
        title = this.title,
        authors = this.authorsDisplayable?.map { it.toAuthor() },
        cover = this.cover,
        status = this.status,
        atUser = this.atUserDisplayable?.toUser(),
        categories = this.categoriesDisplayable?.map { it.toCategory() }
    )
}