package com.szymanski.sharelibrary.features.exchange.di

import com.szymanski.sharelibrary.features.exchange.all.presentation.ExchangesViewModel
import com.szymanski.sharelibrary.features.exchange.data.ExchangeRepositoryImpl
import com.szymanski.sharelibrary.features.exchange.domain.ExchangeRepository
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangesUseCase
import com.szymanski.sharelibrary.features.exchange.domain.usecase.ShareBookUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val exchangesModule = module {
    viewModel {
        ExchangesViewModel(get(), get(), get())
    }

    factory<ExchangeRepository> { ExchangeRepositoryImpl(get(), get()) }


    factory { ShareBookUseCase(get()) }
    factory { GetExchangesUseCase(get()) }
}