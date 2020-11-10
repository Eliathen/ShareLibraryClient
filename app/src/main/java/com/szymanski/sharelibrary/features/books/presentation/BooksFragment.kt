package com.szymanski.sharelibrary.features.books.presentation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class BooksFragment : BaseFragment<BooksViewModel>(R.layout.fragment_books) {

    override val viewModel: BooksViewModel by viewModel()


}