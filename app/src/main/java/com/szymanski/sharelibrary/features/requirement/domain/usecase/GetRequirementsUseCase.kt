package com.szymanski.sharelibrary.features.requirement.domain.usecase

import android.util.Log
import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.requirement.domain.RequirementRepository
import com.szymanski.sharelibrary.features.requirement.domain.model.Requirement

class GetRequirementsUseCase(
    private val requirementRepository: RequirementRepository,
) : BaseUseCase<List<Requirement>, Long>() {
    private val TAG = "GetRequirementsUseCase"

    override suspend fun action(params: Long): List<Requirement> {
        val requirements = requirementRepository.getRequirementsByExchangeId(params)
        Log.d(TAG, "action: $requirements")
        return requirements
    }
}