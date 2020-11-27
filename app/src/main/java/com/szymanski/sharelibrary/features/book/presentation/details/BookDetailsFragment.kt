package com.szymanski.sharelibrary.features.book.presentation.details

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import kotlinx.android.synthetic.main.fragment_book_details.*
import kotlinx.android.synthetic.main.toolbar_base.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BookDetailsFragment : BaseFragment<BookDetailsViewModel>(R.layout.fragment_book_details) {
    override val viewModel: BookDetailsViewModel by viewModel()

    companion object {
        const val BOOK_DETAILS_KEY = "bookDetailsKey"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getParcelable<BookDisplayable>(BOOK_DETAILS_KEY)?.let {
            viewModel.setBook(it)
        }
        initAppBar()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initAppBar() {
        val toolbar = toolbar_base
        (activity as MainActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.book.observe(this) {
            showBookDetails(it)
        }
    }

    private fun showBookDetails(bookDisplayable: BookDisplayable) {
        Glide.with(requireActivity())
            .load(bookDisplayable.cover)
            .into(book_details_cover)
        book_details_title.text = bookDisplayable.title?.replace("\"", "")
        bookDisplayable.authorsDisplayable?.let {
            book_details_author.text = convertAuthorDisplayableListToString(it)
        }

    }

    private fun convertAuthorDisplayableListToString(list: List<AuthorDisplayable>): String {
        var endString = ""
        list.forEach { author ->
            endString += "${author.name} ${author.surname}, "
        }
        endString = endString.trim()
        return endString.substring(0 until endString.length - 1)
    }


}