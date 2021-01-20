package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.user.domain.model.Token

data class TokenResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("tokenType") val tokenType: String,
) {
    fun toToken() = Token(
        accessToken = accessToken,
        tokenType = tokenType
    )
    companion object
}