package com.szymanski.sharelibrary.features.user.di

import com.szymanski.sharelibrary.features.book.domain.usecase.WithdrawBookUseCase
import com.szymanski.sharelibrary.features.user.data.UserRepositoryImpl
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.usecase.EditUserUseCase
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase
import com.szymanski.sharelibrary.features.user.domain.usecase.LoginUserUseCase
import com.szymanski.sharelibrary.features.user.domain.usecase.RegisterUserUseCase
import com.szymanski.sharelibrary.features.user.navigation.UserNavigation
import com.szymanski.sharelibrary.features.user.navigation.UserNavigationImpl
import com.szymanski.sharelibrary.features.user.presentation.login.LoginViewModel
import com.szymanski.sharelibrary.features.user.presentation.otheruser.OtherUserViewModel
import com.szymanski.sharelibrary.features.user.presentation.profile.ProfileViewModel
import com.szymanski.sharelibrary.features.user.presentation.registration.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {

    factory<UserRepository> {
        UserRepositoryImpl(get(), get())
    }
    factory<UserNavigation> {
        UserNavigationImpl(get())
    }

    factory {
        RegisterUserUseCase(get())
    }
    factory {
        LoginUserUseCase(get())
    }
    factory {
        GetUserUseCase(get())
    }
    factory {
        EditUserUseCase(get())
    }
    factory {
        WithdrawBookUseCase(get())
    }

    viewModel {
        RegisterViewModel(get(), get(), get(), get(), get())
    }
    viewModel {
        LoginViewModel(get(), get(), get(), get())
    }
    viewModel {
        ProfileViewModel(get(), get(), get(), get(), get())
    }
    viewModel {
        OtherUserViewModel(get(), get(), get(), get())
    }


}