package com.szymanski.sharelibrary.features.exchange.all.presentation.mapView

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.exchange.all.presentation.ExchangesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ExchangesListViewFragment :
    BaseFragment<ExchangesViewModel>(R.layout.fragment_exchanges_list_view) {
    override val viewModel: ExchangesViewModel by viewModel()

}