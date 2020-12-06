package com.szymanski.sharelibrary.features.requirement.domain

import com.szymanski.sharelibrary.features.requirement.domain.model.Requirement

interface RequirementRepository {

    suspend fun createRequirement(exchangeId: Long, userId: Long): Requirement

    suspend fun getRequirementsByExchangeId(exchangeId: Long): List<Requirement>

}