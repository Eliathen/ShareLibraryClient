package com.szymanski.sharelibrary.features.user.profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.base.EditModeState
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.local.UserStorage
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.domain.usecase.EditUserUseCase
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase
import com.szymanski.sharelibrary.features.user.registration.presentation.model.CoordinateDisplayable
import com.szymanski.sharelibrary.features.user.registration.presentation.model.UserDisplayable

class ProfileViewModel(
    errorMapper: ErrorMapper,
    private val userStorage: UserStorage,
    private val getUserUseCase: GetUserUseCase,
    private val editUserUseCase: EditUserUseCase,
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

    private val _coordinate: MutableLiveData<Coordinate> by lazy {
        MutableLiveData()
    }
    val coordinate: LiveData<CoordinateDisplayable> by lazy {
        _coordinate.map { coordinate ->
            CoordinateDisplayable(coordinate)
        }
    }

    private val _editMode: MutableLiveData<EditModeState> by lazy {
        MutableLiveData<EditModeState>(EditModeState.Inactive)
    }

    val editMode: LiveData<EditModeState> by lazy {
        _editMode
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
                _coordinate.value = it.coordinate
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun editUserDetails(userDisplayable: UserDisplayable) {
        setIdleState()
        editUserUseCase(params = userDisplayable.toUser(), scope = viewModelScope) { result ->
            setPendingState()
            changeEditModeState()
            result.onSuccess {
                _user.value = it
            }
            result.onFailure {
                handleFailure(it)
                _coordinate.value = _user.value?.coordinate
            }
        }
    }

    fun changeEditModeState() {
        _editMode.value =
            if (_editMode.value == EditModeState.Active)
                EditModeState.Inactive
            else
                EditModeState.Active
    }

    fun setNewCoordinates(coordinateDisplayable: CoordinateDisplayable) {
        this._coordinate.value = coordinateDisplayable.toCoordinate()
    }

    fun cancelChanges() {
        this._coordinate.value = _user.value?.coordinate
        changeEditModeState()
    }
}