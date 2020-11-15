package com.szymanski.sharelibrary.features.book.presentation.save

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SaveBookFragment : BaseFragment<SaveBookViewModel>(R.layout.fragment_save_book) {
    override val viewModel: SaveBookViewModel by viewModel()

}