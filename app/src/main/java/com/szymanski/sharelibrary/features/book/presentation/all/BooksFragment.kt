package com.szymanski.sharelibrary.features.book.presentation.all

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_books.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BooksFragment : BaseFragment<BooksViewModel>(R.layout.fragment_books) {

    override val viewModel: BooksViewModel by viewModel()

    override fun initViews() {
        super.initViews()

    }

    private fun initFabListener() {
        save_book_floating_action_button.setOnClickListener {
            viewModel.openSaveBookScreen()
        }
    }
}