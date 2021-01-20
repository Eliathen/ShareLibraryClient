package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.chat.domain.model.Room


data class ChatRoomResponse(
    @SerializedName("id") private val id: Long,
    @SerializedName("sender") private val sender: UserResponse,
    @SerializedName("recipient") private val recipient: UserResponse,
) {

    fun toRoom() = Room(
        id = id,
        sender = sender.toUser(),
        recipient = recipient.toUser(),
    )
    companion object
}
