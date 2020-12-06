package com.szymanski.sharelibrary.features.requirement.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.requirement.domain.RequirementRepository
import com.szymanski.sharelibrary.features.requirement.domain.model.Requirement

class CreateRequirementUseCase(
    private val requirementRepository: RequirementRepository,
) : BaseUseCase<Requirement, Pair<Long, Long>>() {

    //    first param exchangeId, second userId
    override suspend fun action(params: Pair<Long, Long>): Requirement {
        return requirementRepository.createRequirement(params.first, params.second)
    }
}