package com.szymanski.sharelibrary.features.chat.navigation

import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable

interface ChatNavigation {

    fun openChatRoom(room: RoomDisplayable)
}