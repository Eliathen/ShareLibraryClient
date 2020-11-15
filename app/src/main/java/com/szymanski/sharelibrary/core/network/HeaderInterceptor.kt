package com.szymanski.sharelibrary.core.network

import com.szymanski.sharelibrary.core.api.Constant
import com.szymanski.sharelibrary.core.storage.local.UserStorage
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val userStorage: UserStorage) : Interceptor {

    private val urlWithNoAuthorization = arrayListOf(
        Constant.apiUrl + "login",
        Constant.apiUrl + "users/register")

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = userStorage.getUser().token!!.accessToken
        val typeOfToken = userStorage.getUser().token!!.tokenType
        val bearerToken = "$typeOfToken $token"
        if (urlWithNoAuthorization.contains(chain.request().url.toString())) {
            return chain.proceed(chain.request())
        }
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", bearerToken)
            .build()
        return chain.proceed(request)
    }
}