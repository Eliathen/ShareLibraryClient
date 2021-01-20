package com.szymanski.sharelibrary.features.user.domain.model

import com.szymanski.sharelibrary.features.book.domain.model.Book

data class User(
    val id: Long?,
    val email: String? = "",
    val username: String? = "",
    val password: CharArray? = charArrayOf(),
    val name: String? = "",
    val surname: String? = "",
    val coordinates: Coordinate?,
    val books: List<Book>? = listOf(),
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (email != other.email) return false
        if (username != other.username) return false
        if (password != null) {
            if (other.password == null) return false
            if (!password.contentEquals(other.password)) return false
        } else if (other.password != null) return false
        if (name != other.name) return false
        if (surname != other.surname) return false
        if (coordinates != other.coordinates) return false
        if (books != other.books) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + (password?.contentHashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (surname?.hashCode() ?: 0)
        result = 31 * result + (coordinates?.hashCode() ?: 0)
        result = 31 * result + (books?.hashCode() ?: 0)
        return result
    }

    companion object
}
