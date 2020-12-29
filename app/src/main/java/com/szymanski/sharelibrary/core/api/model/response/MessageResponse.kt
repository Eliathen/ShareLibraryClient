package com.szymanski.sharelibrary.core.api.model.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.chat.domain.model.Message
import java.time.LocalDateTime

data class MessageResponse(
    @SerializedName("id") private val id: Long,
    @SerializedName("room") private val chatRoomResponse: ChatRoomResponse,
    @SerializedName("sender") private val sender: UserResponse,
    @SerializedName("recipient") private val recipient: UserResponse,
    @SerializedName("content") private val content: String,
    @SerializedName("timestamp") private val timestamp: String,
) {


    fun toMessage() = Message(
        id = id,
        room = chatRoomResponse.toRoom(),
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

