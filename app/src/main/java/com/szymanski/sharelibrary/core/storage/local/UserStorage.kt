package com.szymanski.sharelibrary.core.storage.local

import com.szymanski.sharelibrary.features.user.domain.model.Login

interface UserStorage {

    fun saveUser(login: Login)

    fun getUser(): Login

    fun getUserId(): Long

    fun saveLoginAndPassword(login: String, password: CharArray)

    fun getLoginAndPassword(): Pair<String, String>
}