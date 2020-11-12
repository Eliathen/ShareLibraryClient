package com.szymanski.sharelibrary.features.users.di

import com.szymanski.sharelibrary.features.users.data.UserRepositoryImpl
import com.szymanski.sharelibrary.features.users.domain.UserRepository
import com.szymanski.sharelibrary.features.users.domain.usecase.LoginUserUseCase
import com.szymanski.sharelibrary.features.users.domain.usecase.RegisterUserUseCase
import com.szymanski.sharelibrary.features.users.login.presentation.LoginViewModel
import com.szymanski.sharelibrary.features.users.navigation.UserNavigation
import com.szymanski.sharelibrary.features.users.navigation.UserNavigationImpl
import com.szymanski.sharelibrary.features.users.profile.presentation.ProfileViewModel
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
    factory {
        LoginUserUseCase(get())
    }

    viewModel {
        RegisterViewModel(get(), get())
    }
    viewModel {
        LoginViewModel(get(), get(), get())
    }
    factory<UserNavigation> {
        UserNavigationImpl(get())
    }

    viewModel {
        ProfileViewModel(get(), get())
    }
}