package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName

class CoverResponse(
    @SerializedName("id") val id: String?,

    @SerializedName("name") val name: String?,

    @SerializedName("type") val type: String?,

    @SerializedName("data") val data: ByteArray,
)

