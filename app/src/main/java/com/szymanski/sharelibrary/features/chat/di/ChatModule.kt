package com.szymanski.sharelibrary.features.chat.di

import com.szymanski.sharelibrary.features.chat.presentation.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {

    viewModel {
        ChatViewModel(get())
    }

}