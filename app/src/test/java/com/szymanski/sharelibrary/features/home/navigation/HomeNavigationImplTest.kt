package com.szymanski.sharelibrary.features.home.navigation

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

internal class HomeNavigationImplTest {


    @Test
    fun `WHEN openChatRoomIfExists is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val homeNavigation = HomeNavigationImpl(fragmentNavigator)
        val room = RoomDisplayable.mock()
        val slot = slot<Pair<String, RoomDisplayable>>()
        //when
        homeNavigation.openChatRoomIfExists(room)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_home_screen_to_chat_room_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe room
    }

    @Test
    fun `WHEN openChatRoomIfNotExists is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val homeNavigation = HomeNavigationImpl(fragmentNavigator)
        val user = UserDisplayable.mock()
        val slot = slot<Pair<String, UserDisplayable>>()
        //when
        homeNavigation.openChatRoomIfNotExists(user)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_home_screen_to_chat_room_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe user
    }

}