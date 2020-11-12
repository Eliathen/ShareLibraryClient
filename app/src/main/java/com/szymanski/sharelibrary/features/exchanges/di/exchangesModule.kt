package com.szymanski.sharelibrary.features.exchanges.di

import com.szymanski.sharelibrary.features.exchanges.presentation.ExchangesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val exchangesModule = module {
    viewModel {
        ExchangesViewModel(get())
    }
}