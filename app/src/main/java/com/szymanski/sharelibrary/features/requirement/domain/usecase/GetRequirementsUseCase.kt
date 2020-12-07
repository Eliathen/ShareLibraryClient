package com.szymanski.sharelibrary.features.requirement.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.requirement.domain.RequirementRepository
import com.szymanski.sharelibrary.features.requirement.domain.model.Requirement

class GetRequirementsUseCase(
    private val requirementRepository: RequirementRepository,
) : BaseUseCase<List<Requirement>, Long>() {
    override suspend fun action(params: Long): List<Requirement> {
        return requirementRepository.getRequirementsByExchangeId(params)

    }
}