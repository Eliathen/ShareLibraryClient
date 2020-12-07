package com.szymanski.sharelibrary.features.exchange.di

import com.szymanski.sharelibrary.features.exchange.all.presentation.ExchangesViewModel
import com.szymanski.sharelibrary.features.exchange.all.presentation.listView.ExchangesListViewAdapter
import com.szymanski.sharelibrary.features.exchange.data.ExchangeRepositoryImpl
import com.szymanski.sharelibrary.features.exchange.details.presentation.ExchangeDetailsViewModel
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.usecase.FinishExchangeUseCase
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangeByIdUseCase
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangesUseCase
import com.szymanski.sharelibrary.features.exchange.domain.usecase.ShareBookUseCase
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigation
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigationImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val exchangesModule = module {
    viewModel { ExchangesViewModel(get(), get(), get(), get()) }
    viewModel { ExchangeDetailsViewModel(get(), get(), get(), get(), get(), get(), get()) }

    factory<ExchangeRepository> { ExchangeRepositoryImpl(get(), get()) }


    factory { ShareBookUseCase(get()) }
    factory { GetExchangesUseCase(get()) }
    factory { FinishExchangeUseCase(get(), get()) }
    factory { GetExchangeByIdUseCase(get()) }

    factory { ExchangesListViewAdapter() }

    factory<ExchangeNavigation> { ExchangeNavigationImpl(get()) }
}