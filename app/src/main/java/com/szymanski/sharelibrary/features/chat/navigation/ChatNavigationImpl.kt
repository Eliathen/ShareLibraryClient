package com.szymanski.sharelibrary.features.chat.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator
import com.szymanski.sharelibrary.features.chat.presentation.room.ChatRoomFragment

class ChatNavigationImpl(
    private val navigator: FragmentNavigator,
) : ChatNavigation {
    override fun openChatRoom(roomId: Long) {
        navigator.navigateTo(R.id.action_navigate_from_chat_screen_to_chat_room_screen,
            Pair(ChatRoomFragment.CHAT_ROOM_KEY, roomId))
    }
}