package com.szymanski.sharelibrary.features.user.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator
import com.szymanski.sharelibrary.features.book.presentation.otheruserbook.OtherUserBooksFragment
import com.szymanski.sharelibrary.features.chat.presentation.room.ChatRoomFragment
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

class UserNavigationImpl(
    private val fragmentNavigator: FragmentNavigator,
) : UserNavigation {
    override fun openRegisterScreen() {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_login_screen_to_register_screen)
    }

    override fun openBooksScreen() {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_login_screen_to_home_screen)

    }

    override fun openBooksScreenAfterRegister() {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_register_screen_to_home_screen)
    }


    override fun openLoginScreenAfterLogout() {
        fragmentNavigator.navigateTo(
            R.id.action_navigate_from_profile_screen_to_login_screen,
        )
    }

    override fun openLoginScreen() {
        fragmentNavigator.navigateTo(R.id.login_screen)
    }

    override fun goBack() {
        fragmentNavigator.goBack()
    }

    override fun openOtherUserBooksScreen(userId: Long) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_other_user_screen_to_other_user_books_screen,
            Pair(OtherUserBooksFragment.OTHER_USER_BOOKS_KEY, userId))
    }

    override fun openChatRoomScreen(otherUser: UserDisplayable) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_other_user_screen_to_chat_room_screen,
            Pair(ChatRoomFragment.CHAT_ROOM_OTHER_USER_KEY, otherUser))
    }
}