package com.szymanski.sharelibrary.features.exchange.di

import com.szymanski.sharelibrary.features.exchange.data.ExchangeRepositoryImpl
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.usecase.*
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigation
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigationImpl
import com.szymanski.sharelibrary.features.exchange.presentation.all.ExchangesViewModel
import com.szymanski.sharelibrary.features.exchange.presentation.details.ExchangeDetailsViewModel
import com.szymanski.sharelibrary.features.exchange.presentation.exchangedbook.ExchangedBookViewModel
import com.szymanski.sharelibrary.features.exchange.presentation.exchangedbook.ExchangedBooksViewAdapter
import com.szymanski.sharelibrary.features.exchange.presentation.listView.ExchangesListViewAdapter
import com.szymanski.sharelibrary.features.home.domain.usecase.ExecuteExchangeUseCase
import com.szymanski.sharelibrary.features.home.domain.usecase.FinishExchangeByIdUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val exchangesModule = module {
    viewModel { ExchangesViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ExchangeDetailsViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ExchangedBookViewModel(get(), get(), get(), get(), get(), get()) }

    factory<ExchangeRepository> { ExchangeRepositoryImpl(get(), get(), get()) }

    factory { ShareBookUseCase(get()) }
    factory { GetExchangesUseCase(get()) }
    factory { FinishExchangeByBookIdUseCase(get(), get()) }
    factory { GetExchangeByIdUseCase(get()) }
    factory { ExecuteExchangeUseCase(get()) }
    factory { GetUserWhichHaveBookWhereAtUserIdIs(get()) }
    factory { GetExchangesByFiltersUseCase(get()) }
    factory { GetUserExchangesUseCase(get()) }
    factory { FinishExchangeByIdUseCase(get()) }

    factory { ExchangesListViewAdapter() }
    factory { ExchangedBooksViewAdapter() }

    factory<ExchangeNavigation> { ExchangeNavigationImpl(get()) }
}