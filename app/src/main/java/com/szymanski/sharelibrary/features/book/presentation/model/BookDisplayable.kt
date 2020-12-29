package com.szymanski.sharelibrary.features.book.presentation.model

import android.os.Parcelable
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookDisplayable(
    val id: Long?,
    val title: String?,
    val authorsDisplayable: List<AuthorDisplayable>?,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookDisplayable

        if (id != other.id) return false
        if (title != other.title) return false
        if (authorsDisplayable != other.authorsDisplayable) return false
        if (cover != null) {
            if (other.cover == null) return false
            if (!cover.contentEquals(other.cover)) return false
        } else if (other.cover != null) return false
        if (status != other.status) return false
        if (atUserDisplayable != other.atUserDisplayable) return false
        if (categoriesDisplayable != other.categoriesDisplayable) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (authorsDisplayable?.hashCode() ?: 0)
        result = 31 * result + (cover?.contentHashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (atUserDisplayable?.hashCode() ?: 0)
        result = 31 * result + (categoriesDisplayable?.hashCode() ?: 0)
        return result
    }

    companion object
}