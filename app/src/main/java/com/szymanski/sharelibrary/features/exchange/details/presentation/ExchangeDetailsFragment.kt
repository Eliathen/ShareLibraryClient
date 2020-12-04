package com.szymanski.sharelibrary.features.exchange.details.presentation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import org.koin.android.ext.android.inject

class ExchangeDetailsFragment :
    BaseFragment<ExchangeDetailsViewModel>(R.layout.fragment_exchange_details) {
    override val viewModel: ExchangeDetailsViewModel by inject()


}