package com.szymanski.sharelibrary.features.home.domain

import com.szymanski.sharelibrary.features.home.domain.model.Requirement

interface RequirementRepository {

    suspend fun createRequirement(exchangeId: Long, userId: Long): Requirement

    suspend fun getRequirementsByExchangeId(exchangeId: Long): List<Requirement>

    suspend fun getUserRequirements(userId: Long): List<Requirement>

}