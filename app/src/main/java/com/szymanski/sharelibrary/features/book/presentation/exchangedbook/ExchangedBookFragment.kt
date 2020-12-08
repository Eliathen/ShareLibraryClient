package com.szymanski.sharelibrary.features.book.presentation.exchangedbook

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangedBookFragment :
    BaseFragment<ExchangedBookViewModel>(R.layout.fragment_exchanged_book) {

    override val viewModel: ExchangedBookViewModel by viewModel()
}