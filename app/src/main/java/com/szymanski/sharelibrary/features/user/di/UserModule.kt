package com.szymanski.sharelibrary.features.user.di

import com.szymanski.sharelibrary.features.user.data.UserRepositoryImpl
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.usecase.LoginUserUseCase
import com.szymanski.sharelibrary.features.user.domain.usecase.RegisterUserUseCase
import com.szymanski.sharelibrary.features.user.login.presentation.LoginViewModel
import com.szymanski.sharelibrary.features.user.navigation.UserNavigation
import com.szymanski.sharelibrary.features.user.navigation.UserNavigationImpl
import com.szymanski.sharelibrary.features.user.profile.presentation.ProfileViewModel
import com.szymanski.sharelibrary.features.user.registration.presentation.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {

    factory<UserRepository> {
        UserRepositoryImpl(get(), get())
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
        LoginViewModel(get(), get(), get(), get())
    }
    factory<UserNavigation> {
        UserNavigationImpl(get())
    }

    viewModel {
        ProfileViewModel(get(), get())
    }
}