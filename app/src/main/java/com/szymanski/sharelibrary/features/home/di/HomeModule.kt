package com.szymanski.sharelibrary.features.home.di

import com.szymanski.sharelibrary.features.home.data.RequirementRepositoryImpl
import com.szymanski.sharelibrary.features.home.domain.RequirementRepository
import com.szymanski.sharelibrary.features.home.domain.usecase.CreateRequirementUseCase
import com.szymanski.sharelibrary.features.home.domain.usecase.GetRequirementByIdUseCase
import com.szymanski.sharelibrary.features.home.domain.usecase.GetUserRequirements
import com.szymanski.sharelibrary.features.home.presentation.all.HomeViewModel
import com.szymanski.sharelibrary.features.home.presentation.requirements.RequirementsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {


    //viewModels
    viewModel { HomeViewModel() }
    viewModel { RequirementsViewModel(get(), get()) }
    //repository
    factory<RequirementRepository> { RequirementRepositoryImpl(get(), get()) }

    //useCase
    factory { CreateRequirementUseCase(get()) }
    factory { GetRequirementByIdUseCase(get()) }
    factory { GetUserRequirements(get()) }
}