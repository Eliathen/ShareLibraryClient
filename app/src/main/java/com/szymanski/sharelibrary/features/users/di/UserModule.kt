package com.szymanski.sharelibrary.features.users.di

import com.szymanski.sharelibrary.features.users.data.UserRepositoryImpl
import com.szymanski.sharelibrary.features.users.domain.UserRepository
import com.szymanski.sharelibrary.features.users.domain.usecase.RegisterUserUseCase
import com.szymanski.sharelibrary.features.users.registration.presentation.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {

    factory<UserRepository> {
        UserRepositoryImpl(get())
    }

    factory {
        RegisterUserUseCase(get())
    }

    viewModel {
        RegisterViewModel(get())
    }
}