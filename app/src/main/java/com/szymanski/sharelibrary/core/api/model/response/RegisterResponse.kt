package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName


data class RegisterResponse(
    @SerializedName("addressView") val address: AddressResponse,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("username") val username: String,
)