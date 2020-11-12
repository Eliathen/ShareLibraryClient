package com.szymanski.sharelibrary.features.users.login.presentation

import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exceptions.ErrorMapper
import com.szymanski.sharelibrary.features.users.domain.usecase.LoginUserUseCase
import com.szymanski.sharelibrary.features.users.login.presentation.model.LoginDisplayable
import com.szymanski.sharelibrary.features.users.navigation.UserNavigation

class LoginViewModel(
    private val errorMapper: ErrorMapper,
    private val userNavigation: UserNavigation,
    private val loginUserUseCase: LoginUserUseCase,
) : BaseViewModel() {


    fun navigateToRegisterScreen() {
        userNavigation.openRegisterScreen()
    }

    private fun navigateToBookScreen() {
        userNavigation.openBooksScreen()
    }

    fun logIn(loginDisplayable: LoginDisplayable) {
        loginUserUseCase(
            viewModelScope, params = loginDisplayable.toLogin()
        ) { it ->
            it.onSuccess {
                navigateToBookScreen()
            }
            it.onFailure {
            }
        }
    }

}