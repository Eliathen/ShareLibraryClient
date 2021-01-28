package com.szymanski.sharelibrary.features.exchange.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigatorImpl
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.mock.mock
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Test

internal class ExchangeNavigationImplTest {

    @Test
    fun `WHEN openExchangeDetails is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val exchangeNavigation = ExchangeNavigationImpl(fragmentNavigator)
        val exchangeId = 1L
        val slot = slot<Pair<String, Long>>()
        //when
        exchangeNavigation.openExchangeDetails(exchangeId)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_exchange_screen_to_exchange_details_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe exchangeId
    }

    @Test
    fun `WHEN openOtherUserScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val exchangeNavigation = ExchangeNavigationImpl(fragmentNavigator)
        val userId = 1L
        val slot = slot<Pair<String, Long>>()
        //when
        exchangeNavigation.openOtherUserScreen(userId)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_exchange_details_screen_to_other_user_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe userId
    }

    @Test
    fun `WHEN navigateBack is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val exchangeNavigation = ExchangeNavigationImpl(fragmentNavigator)
        val room = RoomDisplayable.mock()
        //when
        exchangeNavigation.navigateBack()

        //then
        verify {
            fragmentNavigator.goBack()
        }
    }
}