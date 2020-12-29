package com.szymanski.sharelibrary.features.home.navigation

import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

interface HomeNavigation {
    fun openChatRoomIfExists(room: RoomDisplayable)

    fun openChatRoomIfNotExists(otherUser: UserDisplayable)
}