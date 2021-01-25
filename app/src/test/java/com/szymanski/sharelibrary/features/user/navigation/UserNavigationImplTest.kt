package com.szymanski.sharelibrary.features.user.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigatorImpl
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import com.szymanski.sharelibrary.mock.mock
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Test

internal class UserNavigationImplTest {


    @Test
    fun `WHEN openRegisterScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val userNavigation = UserNavigationImpl(fragmentNavigator)
        //when
        userNavigation.openRegisterScreen()

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_login_screen_to_register_screen
            )
        }
    }

    @Test
    fun `WHEN openBookScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val userNavigation = UserNavigationImpl(fragmentNavigator)
        //when
        userNavigation.openBooksScreen()

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_login_screen_to_home_screen
            )
        }
    }

    @Test
    fun `WHEN openBooksScreenAfterRegister is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val userNavigation = UserNavigationImpl(fragmentNavigator)
        //when
        userNavigation.openBooksScreenAfterRegister()

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_register_screen_to_home_screen
            )
        }
    }

    @Test
    fun `WHEN openLoginScreenAfterLogout is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val userNavigation = UserNavigationImpl(fragmentNavigator)
        //when
        userNavigation.openLoginScreenAfterLogout()

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_profile_screen_to_login_screen
            )
        }
    }

    @Test
    fun `WHEN openLoginScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val userNavigation = UserNavigationImpl(fragmentNavigator)
        //when
        userNavigation.openLoginScreen()

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.login_screen
            )
        }
    }

    @Test
    fun `WHEN openOtherUserBooksScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val userNavigation = UserNavigationImpl(fragmentNavigator)
        val userId = 1L;
        val slot = slot<Pair<String, Long>>()
        //when
        userNavigation.openOtherUserBooksScreen(userId)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_other_user_screen_to_other_user_books_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe userId
    }

    @Test
    fun `WHEN openNotExistingChatRoomScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val userNavigation = UserNavigationImpl(fragmentNavigator)
        val user = UserDisplayable.mock()
        val slot = slot<Pair<String, UserDisplayable>>()

        //when
        userNavigation.openNotExistingChatRoomScreen(user)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_other_user_screen_to_chat_room_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe user
    }

    @Test
    fun `WHEN openExistingChatRoomScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val userNavigation = UserNavigationImpl(fragmentNavigator)
        val room = RoomDisplayable.mock()
        val slot = slot<Pair<String, RoomDisplayable>>()

        //when
        userNavigation.openExistingChatRoomScreen(room)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_other_user_screen_to_chat_room_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe room
    }

    @Test
    fun `WHEN goBack is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val userNavigation = UserNavigationImpl(fragmentNavigator)
        //when
        userNavigation.goBack()

        //then
        verify {
            fragmentNavigator.goBack()
        }
    }
}