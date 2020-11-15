package com.szymanski.sharelibrary.features.exchange.di

import com.szymanski.sharelibrary.features.exchange.presentation.ExchangesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val exchangesModule = module {
    viewModel {
        ExchangesViewModel(get())
    }
}