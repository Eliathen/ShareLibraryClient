package com.szymanski.sharelibrary.features.user.presentation.login

import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.user.domain.usecase.LoginUserUseCase
import com.szymanski.sharelibrary.features.user.navigation.UserNavigation
import com.szymanski.sharelibrary.features.user.presentation.model.LoginDisplayable

class LoginViewModel(
    errorMapper: ErrorMapper,
    private val userNavigation: UserNavigation,
    private val loginUserUseCase: LoginUserUseCase,
    private val userStorage: UserStorage,
) : BaseViewModel(errorMapper) {

    fun logIn(loginDisplayable: LoginDisplayable, isToSaveLoginAndPassword: Boolean) {
        setPendingState()
        loginUserUseCase(
            viewModelScope, params = loginDisplayable.toLogin()
        ) { it ->
            setIdleState()
            it.onSuccess {
                userStorage.saveLoginDetails(it)
                if (isToSaveLoginAndPassword) {
                    userStorage.saveLoginAndPassword(loginDisplayable.userNameOrEmail!!,
                        loginDisplayable.password!!)
                }
                navigateToBookScreen()
            }
            it.onFailure {
                handleFailure(it)
            }
        }
    }

    fun navigateToRegisterScreen() {
        userNavigation.openRegisterScreen()
    }

    private fun navigateToBookScreen() {
        userNavigation.openBooksScreen()
    }


}