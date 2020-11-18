package com.szymanski.sharelibrary.features.user.registration.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.local.UserStorage
import com.szymanski.sharelibrary.features.user.domain.model.Login
import com.szymanski.sharelibrary.features.user.domain.usecase.LoginUserUseCase
import com.szymanski.sharelibrary.features.user.domain.usecase.RegisterUserUseCase
import com.szymanski.sharelibrary.features.user.navigation.UserNavigation
import com.szymanski.sharelibrary.features.user.registration.presentation.model.UserDisplayable

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val userNavigation: UserNavigation,
    private val userStorage: UserStorage,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {

    fun registerUser(userDisplayable: UserDisplayable) {
        val user = userDisplayable.toUser()
        setPendingState()
        registerUserUseCase(viewModelScope, params = user) { result ->
            setIdleState()
            result.onSuccess {
                loginUser(userDisplayable)
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    private fun loginUser(userDisplayable: UserDisplayable) {
        val login = Login(userNameOrEmail = userDisplayable.email,
            password = userDisplayable.password,
            id = null,
            token = null)
        loginUserUseCase(viewModelScope, params = login) { result ->
            result.onSuccess {
                Log.d("RegisterViewModel", userDisplayable.password.contentToString())
                userStorage.saveLoginAndPassword(userDisplayable.username!!,
                    userDisplayable.password!!)
                userStorage.saveUser(it)
                navigateToBookScreen()
            }
        }
    }

    fun navigateToLoginScreen() {
        userNavigation.openLoginScreen()
    }

    private fun navigateToBookScreen() {
        userNavigation.openBooksScreenAfterRegister()
    }
}