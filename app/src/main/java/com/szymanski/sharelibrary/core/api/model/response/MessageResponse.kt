package com.szymanski.sharelibrary.core.api.model.response

import android.annotation.SuppressLint
import com.szymanski.sharelibrary.features.chat.domain.model.Message
import java.time.LocalDateTime

class MessageResponse(
    private val id: Long,

    private val roomResponse: RoomResponse,

    private val sender: UserResponse,

    private val recipient: UserResponse,

    private val content: String,

    private val timestamp: String,
) {


    fun toMessage() = Message(
        id = id,
        room = roomResponse.toRoom(),
        sender = sender.toUser(),
        recipient = recipient.toUser(),
        content = content,
        timestamp = parseStringToLocalDateTime(timestamp)
    )

    @SuppressLint("NewApi")
    private fun parseStringToLocalDateTime(date: String): LocalDateTime {
        return LocalDateTime.parse(date)
    }

}

