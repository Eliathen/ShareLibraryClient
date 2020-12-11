package com.szymanski.sharelibrary.features.chat.presentation.model

import com.szymanski.sharelibrary.features.chat.domain.model.Room
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

data class RoomDisplayable(

    val id: Long? = null,

//    val messages: List<MessageDisplayable>? = null,

    val sender: UserDisplayable? = null,

    val recipient: UserDisplayable? = null,

    ) {
    constructor(room: Room) : this(
        id = room.id,
        sender = UserDisplayable(room.sender!!),
        recipient = UserDisplayable(room.recipient!!)
    )
}