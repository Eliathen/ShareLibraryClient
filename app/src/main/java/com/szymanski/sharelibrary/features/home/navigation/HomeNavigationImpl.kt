package com.szymanski.sharelibrary.features.home.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.features.chat.presentation.room.ChatRoomFragment
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

class HomeNavigationImpl(
    private val fragmentNavigator: FragmentNavigator,
) : HomeNavigation {
    override fun openChatRoomIfExists(room: RoomDisplayable) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_home_screen_to_chat_room_screen,
            Pair(ChatRoomFragment.CHAT_ROOM_KEY, room))
    }

    override fun openChatRoomIfNotExists(otherUser: UserDisplayable) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_home_screen_to_chat_room_screen,
            Pair(ChatRoomFragment.CHAT_ROOM_OTHER_USER_KEY, otherUser))

    }
}