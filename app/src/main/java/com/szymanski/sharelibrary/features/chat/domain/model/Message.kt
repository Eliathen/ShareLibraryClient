package com.szymanski.sharelibrary.features.chat.domain.model

import com.szymanski.sharelibrary.features.user.domain.model.User
import java.time.LocalDateTime

data class Message(
    val id: Long?,

    val room: Room?,

    val sender: User?,

    val recipient: User?,

    val content: String?,

    val timestamp: LocalDateTime?,
) {
    companion object
}
