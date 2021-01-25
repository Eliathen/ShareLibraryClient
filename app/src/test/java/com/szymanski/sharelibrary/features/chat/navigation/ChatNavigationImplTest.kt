package com.szymanski.sharelibrary.features.chat.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigatorImpl
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.mock.mock
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Test

internal class ChatNavigationImplTest {
    @Test
    fun `WHEN openChatRoom is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val chatNavigation = ChatNavigationImpl(fragmentNavigator)
        val room = RoomDisplayable.mock()
        val slot = slot<Pair<String, RoomDisplayable>>()
        //when
        chatNavigation.openChatRoom(room)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_chat_screen_to_chat_room_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe room
    }
}