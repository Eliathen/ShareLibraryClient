package com.szymanski.sharelibrary.features.chat.presentation.room

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.gson.Gson
import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.SendMessageRequest
import com.szymanski.sharelibrary.core.api.model.response.MessageResponse
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.network.HeaderInterceptor
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.chat.domain.model.Message
import com.szymanski.sharelibrary.features.chat.domain.model.Room
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetRoomMessagesUseCase
import com.szymanski.sharelibrary.features.chat.presentation.model.MessageDisplayable
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class ChatRoomViewModel(
    private val userStorage: UserStorage,
    private val getMessagesFromRoomUseCase: GetRoomMessagesUseCase,
    private val api: Api,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {
    private val TAG = "ChatRoomViewModel"

    val userId: Long = userStorage.getUserId()

    private val _room: MutableLiveData<Room> by lazy {
        MutableLiveData()
    }

    private val _messages by lazy {
        MutableLiveData<MutableList<Message>>()
    }

    val messages by lazy {
        _messages.map {
            it.map { message ->
                MessageDisplayable(message)
            }
        }
    }

    fun setRoom(room: RoomDisplayable) {
        _room.value = room.toRoom()
        loadMessages()
    }

    private fun loadMessages() {
        setPendingState()
        getMessagesFromRoomUseCase(
            scope = viewModelScope,
            params = _room.value?.id!!
        ) { result ->
            setIdleState()
            result.onSuccess {
                _messages.value = it.toMutableList()
            }
            result.onFailure { handleFailure(it) }
        }
    }

    private lateinit var stomp: StompClient

    @SuppressLint("CheckResult")
    fun connectSocket() {
        val url = "ws://192.168.8.100:8081/chat"
        val intervalMillis = 100000L
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(userStorage))
            .build()
        stomp = StompClient(client, intervalMillis).apply {
            this.url = url
        }
        val gson = Gson()
        val stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    stomp.url = url
                    val topic = stomp.join("/user/${userId}/queue/messages").subscribe {
                        val message = gson.fromJson(it, MessageResponse::class.java)
                        val all = _messages.value
                        all?.add(message.toMessage())
                        GlobalScope.launch {
                            _messages.postValue(all)
                        }
                        Log.d(TAG, "message: $message")
                    }
                }
                Event.Type.CLOSED -> {
                    Log.d(TAG, "connectSocket: CLOSED")

                }
                Event.Type.ERROR -> {
                    Log.d(TAG, "connectSocket: ERROR")
                }
            }
        }

    }

    fun sendMessage(message: String) {
        val room = _room.value!!
        val mess = SendMessageRequest(room.id,
            userStorage.getUserId(),
            if (room.sender?.id == userStorage.getUserId()) room.recipient?.id else room.sender?.id,
            message
        )
        val gson = Gson()
        val sending = stomp.send("/app/chat", gson.toJson(mess)).subscribe {
            if (it) {
                val messages = _messages.value
                Log.d(TAG, "sendMessage: send")
            } else {
                Log.d(TAG, "sendMessage: Not send")
            }
        }
    }

//    private var webSocket: WebSocket? = null
//    fun sendMessage(){
//        webSocket!!.send("Hello")
//    }
//    fun connectSocket() {
//        val token = userStorage.getLoginDetails().token!!.accessToken
//        val typeOfToken = userStorage.getLoginDetails().token!!.tokenType
//        val bearerToken = "$typeOfToken $token"
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("ws://192.168.8.100:8081/chat")
//            .addHeader("Authorization", bearerToken)
//            .build()
//        webSocket = client.newWebSocket(request, SocketListener())
//    }
//    private class SocketListener : WebSocketListener() {
//        private val TAG = "ChatRoomViewModel"
//        override fun onMessage(webSocket: WebSocket, text: String) {
//            super.onMessage(webSocket, text)
//            Log.d(TAG, "onMessage: $text")
//        }
//
//        override fun onOpen(webSocket: WebSocket, response: Response) {
//            super.onOpen(webSocket, response)
//            Log.d(TAG, "onOpen: Socket opened")
//        }
//
//        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//            super.onFailure(webSocket, t, response)
//            Log.d(TAG, "onFailure: ${t.message}")
//        }
//    }
}

