package com.szymanski.sharelibrary.core.api.model.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.home.domain.model.Requirement
import java.time.LocalDateTime

data class RequirementResponse(
    @SerializedName("id") private val id: Long,
    @SerializedName("exchange") private val exchange: ExchangeResponse?,
    @SerializedName("user") private val user: UserResponse,
    @SerializedName("createTime") private val createTime: String,
    @SerializedName("actual") private val isActual: Boolean,

    ) {

    fun toRequirement() = Requirement(
        id = id,
        exchange = exchange?.toExchange(),
        user = user.toUser(),
        createTime = parseStringToLocalDateTime(createTime),
        isActual = isActual
    )

    @SuppressLint("NewApi")
    private fun parseStringToLocalDateTime(date: String): LocalDateTime {
        return LocalDateTime.parse(date)
    }

    companion object
}