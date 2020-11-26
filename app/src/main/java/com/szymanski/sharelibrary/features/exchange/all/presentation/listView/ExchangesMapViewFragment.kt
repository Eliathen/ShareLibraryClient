package com.szymanski.sharelibrary.features.exchange.all.presentation.listView

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.exchange.all.presentation.ExchangesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangesMapViewFragment :
    BaseFragment<ExchangesViewModel>(R.layout.fragment_exchanges_map_view) {
    override val viewModel: ExchangesViewModel by viewModel()


    override fun initViews() {
        super.initViews()
    }


}