package com.szymanski.sharelibrary.features.home.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.home.domain.RequirementRepository
import com.szymanski.sharelibrary.features.home.domain.model.Requirement

class GetUserRequirements(
    private val requirementRepository: RequirementRepository,
) : BaseUseCase<List<Requirement>, Long>() {
    override suspend fun action(params: Long): List<Requirement> {
        return requirementRepository.getUserRequirements(params)
    }

}
