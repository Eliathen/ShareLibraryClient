package com.szymanski.sharelibrary.features.book.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator


class BookNavigatorImpl(
    private val fragmentNavigator: FragmentNavigator,
) : BookNavigator {
    override fun openSaveBookScreen() {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_books_screen_to_save_book_screen)
    }

    override fun openBooksScreen() {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_save_book_screen_to_books_screen)
    }

}