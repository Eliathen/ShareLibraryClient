package com.szymanski.sharelibrary.features.users.login.presentation.model

import com.szymanski.sharelibrary.features.users.domain.model.Login

class LoginDisplayable(
    private val userNameOrEmail: String?,
    private val password: CharArray?,
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

}