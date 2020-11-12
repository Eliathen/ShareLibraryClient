package com.szymanski.sharelibrary.features.users.registration.presentation

import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.features.users.domain.usecase.RegisterUserUseCase
import com.szymanski.sharelibrary.features.users.navigation.UserNavigation
import com.szymanski.sharelibrary.features.users.registration.presentation.model.UserDisplayable

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val userNavigation: UserNavigation,
) : BaseViewModel() {

    fun registerUser(userDisplayable: UserDisplayable) {
        val user = userDisplayable.toUser()
        registerUserUseCase(viewModelScope, params = user) { result ->
            result.onSuccess {
            }
            result.onFailure {
                it.message?.let { message -> showMessage(message) }
            }
        }
    }

    fun navigateToLoginScreen() {
        userNavigation.openLoginScreen()
    }

    fun navigateToBookScreen() {
        userNavigation.openBooksScreen()
    }


}