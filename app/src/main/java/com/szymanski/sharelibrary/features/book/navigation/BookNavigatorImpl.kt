package com.szymanski.sharelibrary.features.book.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator
import com.szymanski.sharelibrary.features.book.presentation.details.BookDetailsFragment
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.book.presentation.save.SaveBookFragment
import com.szymanski.sharelibrary.features.exchange.presentation.all.ExchangesFragment
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import com.szymanski.sharelibrary.features.user.presentation.otheruser.OtherUserFragment


class BookNavigatorImpl(
    private val fragmentNavigator: FragmentNavigator,
) : BookNavigator {

    override fun openSaveBookScreen(bookDisplayable: BookDisplayable?) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_search_book_screen_to_save_book_screen,
            SaveBookFragment.SAVE_BOOK_SCREEN_KEY to bookDisplayable)
    }

    override fun openBooksScreen() {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_save_book_screen_to_books_screen)
    }

    override fun openSearchBookScreen() {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_books_screen_to_search_book_screen)
    }

    override fun openBookDetailsScreen(bookDisplayable: BookDisplayable) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_books_screen_to_book_details_screen,
            BookDetailsFragment.BOOK_DETAILS_KEY to bookDisplayable)
    }

    override fun openOtherUserBookDetailsScreen(bookDisplayable: BookDisplayable) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_other_user_books_screen_to_book_details_screen,
            BookDetailsFragment.BOOK_DETAILS_KEY to bookDisplayable)
    }

    override fun openOtherUserProfileScreen(otherUserId: Long) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_book_details_screen_to_other_user_screen,
            OtherUserFragment.OTHER_USER_FRAGMENT_KEY to otherUserId)
    }

    override fun displayExchangeOnMap(exchange: ExchangeDisplayable) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_book_details_screen_to_exchanges_screen,
            ExchangesFragment.EXCHANGE_KEY to exchange)
    }
}