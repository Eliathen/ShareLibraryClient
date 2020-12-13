package com.szymanski.sharelibrary.features.chat.presentation.model

import com.szymanski.sharelibrary.features.chat.domain.model.Message
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import java.time.LocalDateTime

data class MessageDisplayable(
    val id: Long?,

    val room: RoomDisplayable?,

    val sender: UserDisplayable?,

    val recipient: UserDisplayable?,

    val content: String?,

    val timestamp: LocalDateTime?,

    ) {
    constructor(message: Message) : this(
        id = message.id,
        room = RoomDisplayable(message.room!!),
        sender = UserDisplayable(message.sender!!),
        recipient = UserDisplayable(message.recipient!!),
        content = message.content,
        timestamp = message.timestamp
    )
}
