package com.szymanski.sharelibrary.core.api

import com.szymanski.sharelibrary.core.api.model.request.LoginRequest
import com.szymanski.sharelibrary.core.api.model.request.RegisterRequest
import com.szymanski.sharelibrary.core.api.model.response.LoginResponse
import com.szymanski.sharelibrary.core.api.model.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse


}