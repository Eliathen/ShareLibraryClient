package com.szymanski.sharelibrary.features.home.di

import com.szymanski.sharelibrary.features.home.data.RequirementRepositoryImpl
import com.szymanski.sharelibrary.features.home.domain.RequirementRepository
import com.szymanski.sharelibrary.features.home.domain.usecase.CreateRequirementUseCase
import com.szymanski.sharelibrary.features.home.domain.usecase.GetCurrentExchangesLinkedWithUserUseCase
import com.szymanski.sharelibrary.features.home.domain.usecase.GetRequirementByExchangeIdUseCase
import com.szymanski.sharelibrary.features.home.domain.usecase.GetUserRequirements
import com.szymanski.sharelibrary.features.home.navigation.HomeNavigation
import com.szymanski.sharelibrary.features.home.navigation.HomeNavigationImpl
import com.szymanski.sharelibrary.features.home.presentation.all.HomeViewModel
import com.szymanski.sharelibrary.features.home.presentation.currentexchanges.CurrentExchangeAdapter
import com.szymanski.sharelibrary.features.home.presentation.currentexchanges.CurrentExchangeViewModel
import com.szymanski.sharelibrary.features.home.presentation.requirements.RequirementsAdapter
import com.szymanski.sharelibrary.features.home.presentation.requirements.RequirementsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {


    //viewModel
    viewModel { HomeViewModel() }
    viewModel { RequirementsViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { CurrentExchangeViewModel(get(), get(), get(), get(), get(), get()) }
    //repository
    factory<RequirementRepository> { RequirementRepositoryImpl(get(), get()) }

    //useCase
    factory { CreateRequirementUseCase(get()) }
    factory { GetRequirementByExchangeIdUseCase(get()) }
    factory { GetUserRequirements(get()) }
    factory { GetCurrentExchangesLinkedWithUserUseCase(get()) }

    //adapter
    factory { RequirementsAdapter() }
    factory { CurrentExchangeAdapter() }

    //navigation
    factory<HomeNavigation> { HomeNavigationImpl(get()) }

}