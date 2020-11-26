package com.szymanski.sharelibrary.core.api

import com.szymanski.sharelibrary.core.api.model.request.*
import com.szymanski.sharelibrary.core.api.model.response.*
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

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Long): UserResponse

    @PUT("users/{id}")
    suspend fun editUser(@Path("id") userId: Long, @Body userRequest: UserRequest): UserResponse

    @POST("users/assignment")
    suspend fun assignBook(@Body assignBookRequest: AssignBookRequest): UserResponse

    @POST("users/withdrawal")
    suspend fun withdrawBook(@Body withdrawBookRequest: WithdrawBookRequest): UserResponse

    @POST("exchanges")
    suspend fun saveExchange(@Body exchangeRequest: SaveExchangeRequest): ExchangeResponse
}