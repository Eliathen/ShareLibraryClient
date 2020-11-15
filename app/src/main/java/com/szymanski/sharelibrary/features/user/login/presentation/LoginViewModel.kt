package com.szymanski.sharelibrary.features.user.login.presentation

import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.local.UserStorage
import com.szymanski.sharelibrary.features.user.domain.usecase.LoginUserUseCase
import com.szymanski.sharelibrary.features.user.login.presentation.model.LoginDisplayable
import com.szymanski.sharelibrary.features.user.navigation.UserNavigation

class LoginViewModel(
    errorMapper: ErrorMapper,
    private val userNavigation: UserNavigation,
    private val loginUserUseCase: LoginUserUseCase,
    private val userStorage: UserStorage,
) : BaseViewModel(errorMapper) {

    fun navigateToRegisterScreen() {
        userNavigation.openRegisterScreen()
    }

    private fun navigateToBookScreen() {
        userNavigation.openBooksScreen()
    }

    fun logIn(loginDisplayable: LoginDisplayable) {
        setPendingState()
        loginUserUseCase(
            viewModelScope, params = loginDisplayable.toLogin()
        ) { it ->
            setIdleState()
            it.onSuccess {
                userStorage.saveUser(it)
                navigateToBookScreen()
            }
            it.onFailure {
                handleFailure(it)
            }
        }
    }


}