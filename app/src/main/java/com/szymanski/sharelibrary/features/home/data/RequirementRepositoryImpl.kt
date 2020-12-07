package com.szymanski.sharelibrary.features.home.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.CreateRequirementRequest
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.exception.callOrThrow
import com.szymanski.sharelibrary.features.home.domain.RequirementRepository
import com.szymanski.sharelibrary.features.home.domain.model.Requirement

class RequirementRepositoryImpl(
    private val api: Api,
    private val errorWrapper: ErrorWrapper,
) : RequirementRepository {


    override suspend fun createRequirement(exchangeId: Long, userId: Long): Requirement {
        return callOrThrow(errorWrapper) {
            val request = CreateRequirementRequest(exchangeId, userId)
            api.createRequirement(request).toRequirement()
        }
    }

    override suspend fun getRequirementsByExchangeId(exchangeId: Long): List<Requirement> {
        return callOrThrow(errorWrapper) {
            api.getRequirementByExchangeId(exchangeId).map { it.toRequirement() }
        }
    }

    override suspend fun getUserRequirements(userId: Long): List<Requirement> {
        return callOrThrow(errorWrapper) {
            api.getUserRequirements(userId).map { it.toRequirement() }
        }
    }
}