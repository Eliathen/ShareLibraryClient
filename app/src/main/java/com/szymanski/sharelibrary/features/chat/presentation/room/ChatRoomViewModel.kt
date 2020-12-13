package com.szymanski.sharelibrary.features.chat.presentation.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.chat.domain.model.Message
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetRoomMessagesUseCase
import com.szymanski.sharelibrary.features.chat.presentation.model.MessageDisplayable

class ChatRoomViewModel(
    userStorage: UserStorage,
    private val getMessagesFromRoomUseCase: GetRoomMessagesUseCase,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {
    private val TAG = "ChatRoomViewModel"

    val userId: Long = userStorage.getUserId()

    private val _roomId: MutableLiveData<Long> by lazy {
        MutableLiveData()
    }
    private val _messages by lazy {
        MutableLiveData<List<Message>>()
    }

    val messages by lazy {
        _messages.map {
            it.map { message ->
                MessageDisplayable(message)
            }
        }
    }

    fun setRoomId(roomId: Long) {
        _roomId.value = roomId
        loadMessages()
    }

    private fun loadMessages() {
        setPendingState()
        getMessagesFromRoomUseCase(
            scope = viewModelScope,
            params = _roomId.value!!
        ) { result ->
            setIdleState()
            result.onSuccess {
                _messages.value = it
            }
            result.onFailure { handleFailure(it) }
        }
    }
}