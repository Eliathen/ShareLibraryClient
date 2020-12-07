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
import com.szymanski.sharelibrary.features.user.navigation.UserNavigation
import com.szymanski.sharelibrary.features.user.registration.presentation.model.UserDisplayable

class OtherUserViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val userStorage: UserStorage,
    private val userNavigation: UserNavigation,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {
    private val _user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    val user: LiveData<UserDisplayable> by lazy {
        _user.map {
            UserDisplayable(it)
        }
    }

    fun loadUser(id: Long) {
        setPendingState()
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

    fun openOtherUserBooksScreen(id: Long) {
        userNavigation.openOtherUserBooksScreen(id)
    }
}