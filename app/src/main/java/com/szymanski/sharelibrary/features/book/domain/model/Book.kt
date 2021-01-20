package com.szymanski.sharelibrary.features.book.domain.model

import com.szymanski.sharelibrary.core.utils.BookCondition
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.user.domain.model.User


data class Book(

    val id: Long?,

    val title: String?,

    val authors: List<Author>?,

    var cover: ByteArray?,

    var status: BookStatus?,

    var atUser: User?,

    var categories: List<Category>?,

    var language: Language?,

    var condition: BookCondition?,

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (id != other.id) return false
        if (title != other.title) return false
        if (authors != other.authors) return false
        if (cover != null) {
            if (other.cover == null) return false
            if (!cover.contentEquals(other.cover)) return false
        } else if (other.cover != null) return false
        if (status != other.status) return false
        if (atUser != other.atUser) return false
        if (categories != other.categories) return false
        if (language != other.language) return false
        if (condition != other.condition) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (authors?.hashCode() ?: 0)
        result = 31 * result + (cover?.contentHashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (atUser?.hashCode() ?: 0)
        result = 31 * result + (categories?.hashCode() ?: 0)
        result = 31 * result + language.hashCode()
        result = 31 * result + condition.hashCode()
        return result
    }

    companion object
}