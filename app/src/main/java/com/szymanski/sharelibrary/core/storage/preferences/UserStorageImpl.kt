package com.szymanski.sharelibrary.core.storage.preferences

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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
    private lateinit var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = activityProvider.currentActivity?.getSharedPreferences(
            preferencesKey,
            MODE_PRIVATE)!!
    }

    override fun saveLoginAndPassword(login: String, password: CharArray) {
        var passwd = password.contentToString().replace(" ", "").replace(",", "")
        passwd = passwd.substring(1, passwd.length - 1)
        sharedPreferences.edit()?.putString(LOGIN_DATA_KEY, login)?.apply()
        sharedPreferences.edit()?.putString(PASSWORD_DATA_KEY, passwd)?.apply()
    }

    override fun getLoginAndPassword(): Pair<String, String> {
        if (!(sharedPreferences.contains(LOGIN_DATA_KEY) && sharedPreferences.contains(
                PASSWORD_DATA_KEY))
        ) {
            return Pair("", "")
        }
        val login = sharedPreferences.getString(LOGIN_DATA_KEY, "")!!
        val password = sharedPreferences.getString(PASSWORD_DATA_KEY, "")!!
        return Pair(login, password)
    }

    override fun clearData() {
        sharedPreferences.edit()?.clear()?.apply()
    }

    override fun saveLoginDetails(login: Login) {
        sharedPreferences.edit()?.putString(USER_KEY, gson.toJson(login))?.apply()
    }

    override fun getLoginDetails(): Login {
        val json = sharedPreferences.getString(USER_KEY, "")!!
        return gson.fromJson(json, Login::class.java)
    }

    override fun getUserId() = getLoginDetails().id!!

}