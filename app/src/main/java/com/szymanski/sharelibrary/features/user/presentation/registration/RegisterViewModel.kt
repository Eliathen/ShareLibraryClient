package com.szymanski.sharelibrary.features.user.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.Login
import com.szymanski.sharelibrary.features.user.domain.usecase.LoginUserUseCase
import com.szymanski.sharelibrary.features.user.domain.usecase.RegisterUserUseCase
import com.szymanski.sharelibrary.features.user.navigation.UserNavigation
import com.szymanski.sharelibrary.features.user.presentation.model.CoordinateDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val userNavigation: UserNavigation,
    private val userStorage: UserStorage,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {

    private val _coordinate: MutableLiveData<Coordinate> by lazy {
        MutableLiveData()
    }
    val coordinate: LiveData<CoordinateDisplayable> by lazy {
        _coordinate.map {
            CoordinateDisplayable(it)
        }
    }

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
                userStorage.saveLoginAndPassword(userDisplayable.username!!,
                    userDisplayable.password!!)
                userStorage.saveLoginDetails(it)
                navigateToBookScreen()
            }
        }
    }

    fun setNewCoordinates(coordinateDisplayable: CoordinateDisplayable) {
        this._coordinate.value = coordinateDisplayable.toCoordinate()
    }

    fun navigateToLoginScreen() {
        userNavigation.openLoginScreen()
    }

    private fun navigateToBookScreen() {
        userNavigation.openBooksScreenAfterRegister()
    }
}