package com.szymanski.sharelibrary.features.chat.presentation.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.chat.domain.model.Room
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetUserRoomsUseCase
import com.szymanski.sharelibrary.features.chat.navigation.ChatNavigation
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable

class ChatViewModel(
    private val userStorage: UserStorage,
    private val getUserRoomsUseCase: GetUserRoomsUseCase,
    private val chatNavigation: ChatNavigation,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {

    private val _rooms: MutableLiveData<List<Room>> by lazy {
        MutableLiveData<List<Room>>().also {
            loadRooms()
        }
    }
    val userId = userStorage.getUserId()

    val rooms: LiveData<List<RoomDisplayable>> by lazy {
        _rooms.map {
            it.map { room ->
                RoomDisplayable(room)
            }
        }
    }

    private fun loadRooms() {
        setPendingState()
        getUserRoomsUseCase(
            scope = viewModelScope,
            params = userStorage.getUserId()
        ) { result ->
            setIdleState()
            result.onSuccess {
                _rooms.value = it
            }
            result.onFailure { handleFailure(it) }
        }
    }

    fun openChatRoom(room: RoomDisplayable) {
        chatNavigation.openChatRoom(room)
    }
}