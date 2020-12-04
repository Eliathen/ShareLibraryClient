package com.szymanski.sharelibrary.features.user.otheruser.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase
import com.szymanski.sharelibrary.features.user.registration.presentation.model.UserDisplayable

class OtherUserViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val userStorage: UserStorage,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {
    private val _user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            loadUser()
        }
    }

    val user: LiveData<UserDisplayable> by lazy {
        _user.map {
            UserDisplayable(it)
        }
    }

    private fun loadUser() {
        setPendingState()
        val id = userStorage.getUserId()
        getUserUseCase(
            scope = viewModelScope,
            params = id
        ) { result ->
            setIdleState()
            result.onSuccess {
                _user.value = it
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }
}