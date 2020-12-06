package com.szymanski.sharelibrary.features.requirement.di

import com.szymanski.sharelibrary.features.requirement.data.RequirementRepositoryImpl
import com.szymanski.sharelibrary.features.requirement.domain.RequirementRepository
import com.szymanski.sharelibrary.features.requirement.domain.usecase.CreateRequirementUseCase
import com.szymanski.sharelibrary.features.requirement.domain.usecase.GetRequirementsUseCase
import org.koin.dsl.module

val requirementModule = module {

    //repository
    factory<RequirementRepository> { RequirementRepositoryImpl(get(), get()) }

    //useCase
    factory { CreateRequirementUseCase(get()) }
    factory { GetRequirementsUseCase(get()) }
}