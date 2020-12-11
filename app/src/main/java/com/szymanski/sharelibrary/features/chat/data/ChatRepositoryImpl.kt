package com.szymanski.sharelibrary.features.chat.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import com.szymanski.sharelibrary.features.chat.domain.model.Room

class ChatRepositoryImpl(
    private val api: Api,
) : ChatRepository {

    override suspend fun getUserChatRooms(userId: Long): List<Room> {
        return api.getRooms(userId).map { it.toRoom() }
    }

}