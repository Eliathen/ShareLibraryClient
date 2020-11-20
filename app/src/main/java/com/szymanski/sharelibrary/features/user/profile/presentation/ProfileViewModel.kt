package com.szymanski.sharelibrary.features.user.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.local.UserStorage
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase
import com.szymanski.sharelibrary.features.user.navigation.UserNavigation

class ProfileViewModel(
    errorMapper: ErrorMapper,
    private val userNavigation: UserNavigation,
    private val userStorage: UserStorage,
    private val getUserUseCase: GetUserUseCase,
) : BaseViewModel(errorMapper) {

    private val _user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            loadUser()
        }
    }

    val user: LiveData<User> by lazy {
        _user
    }

    private fun loadUser() {
        setPendingState()
        val id = userStorage.getUserId()
        getUserUseCase(
            scope = viewModelScope,
            params = id
        ) { result ->
            result.onSuccess {
                _user.value = it
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }
}