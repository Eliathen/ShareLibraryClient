package com.szymanski.sharelibrary.features.user.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.*
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

    override suspend fun getUser(userId: Long): User {
        return callOrThrow(errorWrapper) {
            api.getUser(userId).toUser()
        }
    }

    override suspend fun editUser(user: User): User {
        return callOrThrow(errorWrapper) {
            api.editUser(user.id!!, UserRequest(user)).toUser()
        }
    }

    override suspend fun assignBook(assignBookRequest: AssignBookRequest): User {
        return callOrThrow(errorWrapper) {
            api.assignBook(assignBookRequest).toUser()
        }
    }

    override suspend fun withdrawBook(withdrawBookRequest: WithdrawBookRequest): User {
        return callOrThrow(errorWrapper) {
            api.withdrawBook(withdrawBookRequest).toUser()
        }
    }

    override suspend fun getUsersByBooksWhereAtUserIdIs(params: Long): List<User> {
        return callOrThrow(errorWrapper) {
            api.getUsersByBooksWhereAtUserIdIs(params).map { it.toUser() }
        }
    }


}