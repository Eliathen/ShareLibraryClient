package com.szymanski.sharelibrary.features.users.domain.model

class Login(
    val id: Long?,
    val userNameOrEmail: String?,
    val password: CharArray?,
    val token: Token?,
)