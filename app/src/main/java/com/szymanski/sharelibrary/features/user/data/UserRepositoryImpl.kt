package com.szymanski.sharelibrary.features.user.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.LoginRequest
import com.szymanski.sharelibrary.core.api.model.request.RegisterRequest
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.exception.callOrThrow
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.Login
import com.szymanski.sharelibrary.features.user.domain.model.User

class UserRepositoryImpl(
    private val api: Api,
    private val errorWrapper: ErrorWrapper,
) : UserRepository {

    override suspend fun registerUser(user: User): User {
        return callOrThrow(errorWrapper) {
            api.registerUser(RegisterRequest(user)).toUser()
        }
    }

    override suspend fun login(login: Login): Login {
        return callOrThrow(errorWrapper) { api.login(LoginRequest(login)).toLogin() }
    }


}