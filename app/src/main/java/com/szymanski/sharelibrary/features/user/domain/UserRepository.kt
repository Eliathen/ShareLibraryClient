package com.szymanski.sharelibrary.features.user.domain

import com.szymanski.sharelibrary.features.user.domain.model.Login
import com.szymanski.sharelibrary.features.user.domain.model.User

interface UserRepository {

    suspend fun registerUser(user: User): User

    suspend fun login(login: Login): Login

}