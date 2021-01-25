package com.szymanski.sharelibrary.features.book.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigatorImpl
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import com.szymanski.sharelibrary.mock.mock
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Test

internal class BookNavigatorImplTest {

    @Test
    fun `WHEN openSaveBookScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val bookNavigator = BookNavigatorImpl(fragmentNavigator)
        val book = BookDisplayable.mock()
        val slot = slot<Pair<String, BookDisplayable>>()
        //when
        bookNavigator.openSaveBookScreen(book)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_search_book_screen_to_save_book_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe book
    }

    @Test
    fun `WHEN openBooksScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val bookNavigator = BookNavigatorImpl(fragmentNavigator)
        //when
        bookNavigator.openBooksScreen()

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_save_book_screen_to_books_screen,
            )
        }
    }

    @Test
    fun `WHEN openSearchBookScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val bookNavigator = BookNavigatorImpl(fragmentNavigator)
        //when
        bookNavigator.openSearchBookScreen()

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_books_screen_to_search_book_screen,
            )
        }
    }

    @Test
    fun `WHEN openBookDetailsScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val bookNavigator = BookNavigatorImpl(fragmentNavigator)
        val book = BookDisplayable.mock()
        val slot = slot<Pair<String, BookDisplayable>>()
        //when
        bookNavigator.openBookDetailsScreen(book)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_books_screen_to_book_details_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe book
    }

    @Test
    fun `WHEN openOtherUserBookDetailsScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val bookNavigator = BookNavigatorImpl(fragmentNavigator)
        val book = BookDisplayable.mock()
        val slot = slot<Pair<String, BookDisplayable>>()
        //when
        bookNavigator.openOtherUserBookDetailsScreen(book)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_other_user_books_screen_to_book_details_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe book
    }

    @Test
    fun `WHEN openOtherUserProfileScreen is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val bookNavigator = BookNavigatorImpl(fragmentNavigator)
        val userId = 1L
        val slot = slot<Pair<String, Long>>()
        //when
        bookNavigator.openOtherUserProfileScreen(userId)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_book_details_screen_to_other_user_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe userId
    }

    @Test
    fun `WHEN displayExchangeOnMap is called THAN invoke navigateTo method with appropriate action as argument`() {
        //given
        val fragmentNavigator = mockk<FragmentNavigatorImpl>(relaxed = true)
        val bookNavigator = BookNavigatorImpl(fragmentNavigator)
        val exchange = ExchangeDisplayable.mock()
        val slot = slot<Pair<String, ExchangeDisplayable>>()
        //when
        bookNavigator.displayExchangeOnMap(exchange)

        //then
        verify {
            fragmentNavigator.navigateTo(
                R.id.action_navigate_from_book_details_screen_to_exchanges_screen,
                capture(slot)
            )
        }
        slot.captured.second shouldBe exchange
    }

}