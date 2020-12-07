package com.szymanski.sharelibrary.features.home.domain.model

import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.user.domain.model.User
import java.time.LocalDateTime

data class Requirement(
    val id: Long?,
    val exchange: Exchange?,
    val user: User?,
    val createTime: LocalDateTime?,
    val isActual: Boolean,
)