package com.szymanski.sharelibrary.features.exchange.navigation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator
import com.szymanski.sharelibrary.features.exchange.details.presentation.ExchangeDetailsFragment

class ExchangeNavigationImpl(
    private val fragmentNavigator: FragmentNavigator,
) : ExchangeNavigation {
    override fun openExchangeDetails(exchangeId: Long) {
        fragmentNavigator.navigateTo(R.id.action_navigate_from_exchange_screen_to_exchange_details_screen,
            param = Pair<String, Long>(ExchangeDetailsFragment.EXCHANGE_DETAILS_KEY, exchangeId)
        )
    }
}