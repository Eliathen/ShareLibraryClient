package com.szymanski.sharelibrary.features.chat.domain.model

import com.szymanski.sharelibrary.features.user.domain.model.User

data class Room(

    val id: Long? = null,

    val sender: User? = null,

    val recipient: User? = null,
) {
    companion object
}