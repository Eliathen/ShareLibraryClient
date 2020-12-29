package com.szymanski.sharelibrary.features.user.presentation.model

import com.szymanski.sharelibrary.features.user.domain.model.Login

data class LoginDisplayable(
    val userNameOrEmail: String?,
    val password: CharArray?,
) {
    constructor(login: Login) : this(
        userNameOrEmail = login.userNameOrEmail,
        password = login.password
    )

    fun toLogin() = Login(
        id = null,
        userNameOrEmail = userNameOrEmail,
        password = password,
        token = null
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginDisplayable

        if (userNameOrEmail != other.userNameOrEmail) return false
        if (password != null) {
            if (other.password == null) return false
            if (!password.contentEquals(other.password)) return false
        } else if (other.password != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userNameOrEmail?.hashCode() ?: 0
        result = 31 * result + (password?.contentHashCode() ?: 0)
        return result
    }

    companion object
}