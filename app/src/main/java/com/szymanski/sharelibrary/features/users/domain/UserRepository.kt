package com.szymanski.sharelibrary.features.users.domain

import com.szymanski.sharelibrary.features.users.domain.model.Login
import com.szymanski.sharelibrary.features.users.domain.model.User

interface UserRepository {

    suspend fun registerUser(user: User): User

    suspend fun login(login: Login): Login

}