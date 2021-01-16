package com.szymanski.sharelibrary.core.api.model.request

import com.szymanski.sharelibrary.features.user.domain.model.Login


data class LoginRequest(
    val userNameOrEmail: String,
    val password: CharArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginRequest

        if (userNameOrEmail != other.userNameOrEmail) return false
        if (!password.contentEquals(other.password)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userNameOrEmail.hashCode()
        result = 31 * result + password.contentHashCode()
        return result
    }

    constructor(login: Login) : this(
        userNameOrEmail = login.userNameOrEmail!!,
        password = login.password!!
    )

    companion object
}