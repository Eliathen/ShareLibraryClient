package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.user.domain.model.Login


data class LoginResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("userName") val userName: String,
    @SerializedName("response") val response: TokenResponse,

    ) {
    fun toLogin() = Login(
        id = this.id,
        userNameOrEmail = this.userName,
        password = null,
        token = response.toToken()
    )
}

