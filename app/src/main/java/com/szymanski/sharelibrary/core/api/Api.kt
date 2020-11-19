package com.szymanski.sharelibrary.core.api

import com.szymanski.sharelibrary.core.api.model.request.LoginRequest
import com.szymanski.sharelibrary.core.api.model.request.RegisterRequest
import com.szymanski.sharelibrary.core.api.model.response.BookResponse
import com.szymanski.sharelibrary.core.api.model.response.LoginResponse
import com.szymanski.sharelibrary.core.api.model.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface Api {

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("books")
    @Multipart
    @JvmSuppressWildcards
    suspend fun saveBook(
        @Part("title") title: String,
        @Part image: MultipartBody.Part,
        @PartMap authors: Map<String, RequestBody>,
        @Part("userId") userId: Long,
    )

    @GET("books/user/{userId}")
    suspend fun getUsersBook(@Path("userId") userId: Long): List<BookResponse>

    @GET("books")
    suspend fun searchBooks(@Query("q") query: String): List<BookResponse>

    @GET("books/{id}/cover")
    @Streaming
    suspend fun getCover(@Path("id") bookId: Long): ResponseBody
}