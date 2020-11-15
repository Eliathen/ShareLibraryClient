package com.szymanski.sharelibrary.features.user.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator

class UserNavigationImpl(
    private val fragmentNavigator: FragmentNavigator,
) : UserNavigation {
    override fun openRegisterScreen() {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_login_screen_to_register_screen)
    }

    override fun openBooksScreen() {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_login_screen_to_book_screen)
    }

    override fun openLoginScreen() {
        fragmentNavigator.navigateTo(R.id.login_screen)
    }

    override fun goBack() {
        fragmentNavigator.goBack()
    }
}