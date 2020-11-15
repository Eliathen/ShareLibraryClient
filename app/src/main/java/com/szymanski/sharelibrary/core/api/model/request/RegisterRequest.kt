package com.szymanski.sharelibrary.core.api.model.request

import com.szymanski.sharelibrary.features.user.domain.model.User


data class RegisterRequest(
    val defaultAddress: AddressRequest?,
    val email: String?,
    val name: String?,
    val password: CharArray?,
    val surname: String?,
    val username: String?,
) {
    constructor(user: User) : this(
        defaultAddress = user.address?.let { AddressRequest(it) },
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

        if (defaultAddress != other.defaultAddress) return false
        if (email != other.email) return false
        if (name != other.name) return false
        if (!password.contentEquals(other.password)) return false
        if (surname != other.surname) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = defaultAddress.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + surname.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}

