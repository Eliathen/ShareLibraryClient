package com.szymanski.sharelibrary.features.user.domain

import com.szymanski.sharelibrary.core.api.model.request.AssignBookRequest
import com.szymanski.sharelibrary.core.api.model.request.WithdrawBookRequest
import com.szymanski.sharelibrary.features.user.domain.model.Login
import com.szymanski.sharelibrary.features.user.domain.model.User

interface UserRepository {

    suspend fun registerUser(user: User): User

    suspend fun login(login: Login): Login

    suspend fun getUser(userId: Long): User

    suspend fun editUser(user: User): User

    suspend fun assignBook(assignBookRequest: AssignBookRequest): User

    suspend fun withdrawBook(withdrawBookRequest: WithdrawBookRequest): User

}