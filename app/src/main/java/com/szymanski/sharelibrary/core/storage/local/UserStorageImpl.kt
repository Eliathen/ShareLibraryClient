package com.szymanski.sharelibrary.core.storage.local

import com.google.gson.Gson
import com.szymanski.sharelibrary.core.provider.ActivityProvider
import com.szymanski.sharelibrary.features.user.domain.model.Login

class UserStorageImpl(
    private val activityProvider: ActivityProvider,
    private val gson: Gson,
) : UserStorage {

    private val preferencesKey = "ShareLibraryPreferences"
    private val USER_KEY = "user"
    private val LOGIN_DATA_KEY = "login"
    private val PASSWORD_DATA_KEY = "password"


    private fun getSharedPreferences() = activityProvider.currentActivity?.getSharedPreferences(
        preferencesKey,
        android.content.Context.MODE_PRIVATE)

    override fun saveLoginAndPassword(login: String, password: CharArray) {
        var passwd = password.contentToString().replace(" ", "").replace(",", "")
        passwd = passwd.substring(1, passwd.length - 1)
        getSharedPreferences()?.edit()?.putString(LOGIN_DATA_KEY, login)?.apply()
        getSharedPreferences()?.edit()?.putString(PASSWORD_DATA_KEY, passwd)?.apply()
    }

    override fun getLoginAndPassword(): Pair<String, String> {
        if (!(getSharedPreferences()!!.contains(LOGIN_DATA_KEY) && getSharedPreferences()!!.contains(
                PASSWORD_DATA_KEY))
        ) {
            return Pair("", "")
        }
        val login = getSharedPreferences()!!.getString(LOGIN_DATA_KEY, "")!!
        val password = getSharedPreferences()!!.getString(PASSWORD_DATA_KEY, "")!!
        return Pair(login, password)
    }

    override fun saveUser(login: Login) {
        getSharedPreferences()?.edit()?.putString(USER_KEY, gson.toJson(login))?.apply()
    }

    override fun getUser(): Login {
        val json = getSharedPreferences()?.getString(USER_KEY, "")
        return gson.fromJson(json, Login::class.java)
    }

    override fun getUserId() = getUser().id!!

}