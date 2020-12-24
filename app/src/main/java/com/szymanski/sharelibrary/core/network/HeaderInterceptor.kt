package com.szymanski.sharelibrary.core.network

import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.utils.API_URL
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val userStorage: UserStorage) : Interceptor {

    private val urlWithNoAuthorization = arrayListOf(
        API_URL + "login",
        API_URL + "users/register")

    override fun intercept(chain: Interceptor.Chain): Response {
        if (urlWithNoAuthorization.contains(chain.request().url.toString())) {
            return chain.proceed(chain.request())
        }
        val token = userStorage.getLoginDetails().token!!.accessToken
        val typeOfToken = userStorage.getLoginDetails().token!!.tokenType
        val bearerToken = "$typeOfToken $token"
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", bearerToken)
            .build()
        return chain.proceed(request)
    }
}