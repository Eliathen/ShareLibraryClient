package com.szymanski.sharelibrary.core.api.model.response

import com.szymanski.sharelibrary.features.chat.domain.model.Room


data class RoomResponse(

    private val id: Long,

    private val sender: UserResponse,

    private val recipient: UserResponse,

    ) {

    fun toRoom() = Room(
        id = id,
        sender = sender.toUser(),
        recipient = recipient.toUser(),
    )
}
