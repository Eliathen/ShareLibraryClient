package com.szymanski.sharelibrary.features.exchange.presentation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangesFragment : BaseFragment<ExchangesViewModel>(R.layout.fragment_exchange) {

    override val viewModel: ExchangesViewModel by viewModel()


    override fun initViews() {
        super.initViews()
    }

    override fun initObservers() {
        super.initObservers()
    }

    override fun onIdleState() {
        super.onIdleState()
    }

    override fun onPendingState() {
        super.onPendingState()
    }
}