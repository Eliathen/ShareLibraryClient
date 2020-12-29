package com.szymanski.sharelibrary.core.storage.preferences

import com.szymanski.sharelibrary.features.user.domain.model.Login

interface UserStorage {

    fun saveLoginDetails(login: Login)

    fun getLoginDetails(): Login

    fun getUserId(): Long

    fun saveLoginAndPassword(login: String, password: CharArray)

    fun getLoginAndPassword(): Pair<String, String>

    fun clearData()
}