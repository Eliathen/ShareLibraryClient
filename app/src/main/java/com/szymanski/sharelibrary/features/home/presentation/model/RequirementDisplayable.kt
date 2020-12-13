package com.szymanski.sharelibrary.features.home.presentation.model

import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import com.szymanski.sharelibrary.features.home.domain.model.Requirement
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import java.time.LocalDateTime

class RequirementDisplayable(
    val id: Long?,
    val exchange: ExchangeDisplayable?,
    val user: UserDisplayable?,
    val createTime: LocalDateTime?,
    val isActual: Boolean?,
) {
    constructor(requirement: Requirement) : this(
        id = requirement.id,
        exchange = requirement.exchange?.let { ExchangeDisplayable(it) },
        user = requirement.user?.let { UserDisplayable(it) },
        createTime = requirement.createTime,
        isActual = requirement.isActual
    )

    fun toRequirement() = Requirement(
        id = id,
        user = user?.toUser(),
        createTime = createTime,
        isActual = isActual!!,
        exchange = exchange?.toExchange()
    )
}