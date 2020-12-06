package com.szymanski.sharelibrary.features.requirement.data

import android.util.Log
import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.CreateRequirementRequest
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.exception.callOrThrow
import com.szymanski.sharelibrary.features.requirement.domain.RequirementRepository
import com.szymanski.sharelibrary.features.requirement.domain.model.Requirement

class RequirementRepositoryImpl(
    private val api: Api,
    private val errorWrapper: ErrorWrapper,
) : RequirementRepository {

    private val TAG = "RequirementRepositoryIm"

    override suspend fun createRequirement(exchangeId: Long, userId: Long): Requirement {
        return callOrThrow(errorWrapper) {
            val request = CreateRequirementRequest(exchangeId, userId)
            api.createRequirement(request).toRequirement()
        }
    }

    override suspend fun getRequirementsByExchangeId(exchangeId: Long): List<Requirement> {
        return callOrThrow(errorWrapper) {
            val result = api.getRequirementByExchangeId(exchangeId).map { it.toRequirement() }
            Log.d(TAG, "getRequirementsByExchangeId: $result")
            result
        }
    }
}