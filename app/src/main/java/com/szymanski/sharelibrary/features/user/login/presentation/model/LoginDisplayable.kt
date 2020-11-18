package com.szymanski.sharelibrary.features.user.login.presentation.model

import com.szymanski.sharelibrary.features.user.domain.model.Login

class LoginDisplayable(
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

}