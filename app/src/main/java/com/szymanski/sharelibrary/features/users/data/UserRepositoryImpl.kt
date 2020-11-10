package com.szymanski.sharelibrary.features.users.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.LoginRequest
import com.szymanski.sharelibrary.core.api.model.request.RegisterRequest
import com.szymanski.sharelibrary.features.users.domain.UserRepository
import com.szymanski.sharelibrary.features.users.domain.model.Login
import com.szymanski.sharelibrary.features.users.domain.model.User

class UserRepositoryImpl(
    private val api: Api,
) : UserRepository {


    override suspend fun registerUser(user: User) {
        api.registerUser(RegisterRequest(user))
    }

    override suspend fun login(login: Login) {
        api.login(LoginRequest(login))
    }

}