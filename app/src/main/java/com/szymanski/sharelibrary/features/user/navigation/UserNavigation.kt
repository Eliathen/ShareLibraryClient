package com.szymanski.sharelibrary.features.user.navigation

import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

interface UserNavigation {

    fun openRegisterScreen()

    fun openBooksScreen()

    fun openLoginScreen()

    fun openBooksScreenAfterRegister()

    fun openLoginScreenAfterLogout()

    fun goBack()

    fun openOtherUserBooksScreen(userId: Long)

    fun openNotExistingChatRoomScreen(otherUser: UserDisplayable)

    fun openExistingChatRoomScreen(room: RoomDisplayable)

}