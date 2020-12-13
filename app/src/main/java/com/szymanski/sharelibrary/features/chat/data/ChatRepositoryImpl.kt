package com.szymanski.sharelibrary.features.chat.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.exception.callOrThrow
import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import com.szymanski.sharelibrary.features.chat.domain.model.Message
import com.szymanski.sharelibrary.features.chat.domain.model.Room

class ChatRepositoryImpl(
    private val api: Api,
    private val errorWrapper: ErrorWrapper,
) : ChatRepository {

    override suspend fun getUserChatRooms(userId: Long): List<Room> {
        return callOrThrow(errorWrapper) { api.getRooms(userId).map { it.toRoom() } }
    }

    override suspend fun getRoomMessages(roomId: Long): List<Message> {
        return callOrThrow(errorWrapper) { api.getRoomMessages(roomId).map { it.toMessage() } }
    }

}