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


    private fun getSharedPreferences() = activityProvider.currentActivity?.getSharedPreferences(
        preferencesKey,
        android.content.Context.MODE_PRIVATE)

    override fun saveUser(login: Login) {
        getSharedPreferences()?.edit()?.putString(USER_KEY, gson.toJson(login))?.apply()
    }

    override fun getUser(): Login {
        val json = getSharedPreferences()?.getString(USER_KEY, "")
        return gson.fromJson(json, Login::class.java)
    }


}