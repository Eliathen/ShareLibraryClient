package com.szymanski.sharelibrary.features.chat.domain

import com.szymanski.sharelibrary.features.chat.domain.model.Message
import com.szymanski.sharelibrary.features.chat.domain.model.Room

interface ChatRepository {

    suspend fun getUserChatRooms(userId: Long): List<Room>
    suspend fun getRoomMessages(roomId: Long): List<Message>
    suspend fun getRoomBySenderIdAndRecipientId(senderId: Long, recipientId: Long): Room

}
