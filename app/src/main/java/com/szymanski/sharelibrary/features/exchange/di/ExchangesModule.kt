package com.szymanski.sharelibrary.features.exchange.di

import com.szymanski.sharelibrary.features.exchange.data.ExchangeRepositoryImpl
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.usecase.*
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigation
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigationImpl
import com.szymanski.sharelibrary.features.exchange.presentation.all.ExchangesViewModel
import com.szymanski.sharelibrary.features.exchange.presentation.details.ExchangeDetailsViewModel
import com.szymanski.sharelibrary.features.exchange.presentation.exchangedbook.ExchangedBookViewModel
import com.szymanski.sharelibrary.features.exchange.presentation.listView.ExchangesListViewAdapter
import com.szymanski.sharelibrary.features.home.domain.usecase.ExecuteExchangeUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val exchangesModule = module {
    //viewModel
    viewModel { ExchangesViewModel(get(), get(), get(), get(), get()) }
    viewModel { ExchangeDetailsViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ExchangedBookViewModel(get(), get(), get(), get()) }

    //repository
    factory<ExchangeRepository> { ExchangeRepositoryImpl(get(), get()) }

    //useCase
    factory { ShareBookUseCase(get()) }
    factory { GetExchangesUseCase(get()) }
    factory { FinishExchangeUseCase(get(), get()) }
    factory { GetExchangeByIdUseCase(get()) }
    factory { ExecuteExchangeUseCase(get()) }
    factory { GetExchangesByAtUserId(get()) }
    //adapter
    factory { ExchangesListViewAdapter() }

    //navigation
    factory<ExchangeNavigation> { ExchangeNavigationImpl(get()) }
}