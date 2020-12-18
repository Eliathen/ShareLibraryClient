package com.szymanski.sharelibrary.features.chat.presentation.model

import android.os.Parcelable
import com.szymanski.sharelibrary.features.chat.domain.model.Room
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoomDisplayable(

    val id: Long? = null,

//    val messages: List<MessageDisplayable>? = null,

    val sender: UserDisplayable? = null,

    val recipient: UserDisplayable? = null,

    ) : Parcelable {
    constructor(room: Room) : this(
        id = room.id,
        sender = UserDisplayable(room.sender!!),
        recipient = UserDisplayable(room.recipient!!)
    )

    fun toRoom() = Room(
        id = id,
        sender = sender?.toUser(),
        recipient = recipient?.toUser()
    )
}