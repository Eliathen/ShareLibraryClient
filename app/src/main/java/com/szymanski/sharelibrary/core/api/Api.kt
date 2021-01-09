package com.szymanski.sharelibrary.core.api

import com.szymanski.sharelibrary.core.api.model.request.*
import com.szymanski.sharelibrary.core.api.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("categories")
    suspend fun getCategories(): List<CategoryResponse>

    @POST("books")
    @Multipart
    @JvmSuppressWildcards
    suspend fun saveBook(
        @Part("title") title: String,
        @Part image: MultipartBody.Part,
        @PartMap authors: Map<String, RequestBody>,
        @PartMap categories: Map<String, RequestBody>,
        @Part("userId") userId: Long,
        @PartMap language: Map<String, RequestBody>,
        @Part("conditionId") conditionId: Int,
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

    @GET("exchanges")
    suspend fun getExchangesByUserId(@Query("userId") userId: Long): List<ExchangeResponse>

    @POST("exchanges/{id}/end")
    suspend fun finishExchange(@Path("id") exchangeId: Long?): Response<Unit>

    @GET("exchanges/{id}")
    suspend fun getExchangeById(@Path("id") exchangeId: Long): ExchangeResponse

    @POST("requirements")
    suspend fun createRequirement(@Body createRequirementRequest: CreateRequirementRequest): RequirementResponse

    @GET("exchanges/{id}/requirements")
    suspend fun getRequirementByExchangeId(@Path("id") exchangeId: Long): List<RequirementResponse>

    @GET("requirements/{id}")
    suspend fun getUserRequirements(@Path("id") userId: Long): List<RequirementResponse>

    @POST("exchanges/execution")
    suspend fun executeExchange(@Body executeExchangeRequest: ExecuteExchangeRequest): ExchangeResponse

    @GET("exchanges/withUser/{id}")
    suspend fun exchangesByAtUserId(@Path("id") userId: Long): List<ExchangeResponse>

    @GET("chat/rooms/{id}")
    suspend fun getRooms(@Path("id") userId: Long): List<ChatRoomResponse>

    @GET("chat/rooms/{roomId}/messages")
    suspend fun getRoomMessages(@Path("roomId") roomId: Long): List<MessageResponse>

    @GET("exchanges/filter")
    suspend fun getExchangesWithFilter(
        @Query("lat") latitude: Double,
        @Query("long") longitude: Double,
        @Query("rad") radius: Double?,
        @Query("cat") categories: List<String>?,
        @Query("q") query: String?,
        @Query("lan") language: Int?,
        @Query("con") conditions: List<Int>?,
    ): List<ExchangeResponse>

    @GET("chat/rooms")
    suspend fun getRoomBySenderIdAndRecipientId(
        @Query("sender") senderId: Long,
        @Query("recipient") recipient: Long,
    ): ChatRoomResponse

    @GET("books/languages")
    suspend fun getLanguageList(): List<LanguageResponse>

    @GET("books/{userId}/exchanged")
    suspend fun getUsersByBooksWhereAtUserIdIs(@Path("userId") userId: Long): List<UserResponse>

}