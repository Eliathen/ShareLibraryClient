package com.szymanski.sharelibrary.features.users.registration.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.features.users.domain.usecase.RegisterUserUseCase
import com.szymanski.sharelibrary.features.users.registration.presentation.model.UserDisplayable

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
) : BaseViewModel() {

    val _user: MutableLiveData<UserDisplayable> by lazy {
        MutableLiveData<UserDisplayable>()
    }

    val user: LiveData<UserDisplayable> by lazy {
        _user
    }


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


}