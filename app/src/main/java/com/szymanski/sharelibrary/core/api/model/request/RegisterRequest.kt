package com.szymanski.sharelibrary.core.api.model.request

import com.szymanski.sharelibrary.features.user.domain.model.User


data class RegisterRequest(
    val coordinates: CoordinatesRequest?,
    val email: String?,
    val name: String?,
    val password: CharArray?,
    val surname: String?,
    val username: String?,
) {
    constructor(user: User) : this(
        coordinates = user.coordinates?.let { CoordinatesRequest(it) },
        email = user.email,
        name = user.name,
        password = user.password,
        surname = user.surname,
        username = user.username
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegisterRequest

        if (email != other.email) return false
        if (name != other.name) return false
        if (!password.contentEquals(other.password)) return false
        if (surname != other.surname) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coordinates?.hashCode() ?: 0
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (password?.contentHashCode() ?: 0)
        result = 31 * result + (surname?.hashCode() ?: 0)
        result = 31 * result + (username?.hashCode() ?: 0)
        return result
    }

}

