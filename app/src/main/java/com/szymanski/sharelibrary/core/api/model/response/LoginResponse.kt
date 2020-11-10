package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("response") val response: TokenResponse,
    @SerializedName("userName") val userName: String,
)

