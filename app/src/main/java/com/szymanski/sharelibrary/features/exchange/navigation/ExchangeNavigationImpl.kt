package com.szymanski.sharelibrary.features.exchange.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator
import com.szymanski.sharelibrary.features.exchange.details.presentation.ExchangeDetailsFragment
import com.szymanski.sharelibrary.features.user.otheruser.presentation.OtherUserFragment

class ExchangeNavigationImpl(
    private val fragmentNavigator: FragmentNavigator,
) : ExchangeNavigation {
    override fun openExchangeDetails(exchangeId: Long) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_exchange_screen_to_exchange_details_screen,
            param = Pair(ExchangeDetailsFragment.EXCHANGE_DETAILS_KEY, exchangeId)
        )
    }

    override fun openOtherUserScreen(userId: Long) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_exchange_details_screen_to_other_user_screen,
            param = Pair(OtherUserFragment.OTHER_USER_FRAGMENT_KEY, userId))
    }
}