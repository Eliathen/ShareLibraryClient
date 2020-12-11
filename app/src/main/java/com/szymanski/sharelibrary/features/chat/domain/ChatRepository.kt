package com.szymanski.sharelibrary.features.chat.domain

import com.szymanski.sharelibrary.features.chat.domain.model.Room

interface ChatRepository {

    suspend fun getUserChatRooms(userId: Long): List<Room>

}
