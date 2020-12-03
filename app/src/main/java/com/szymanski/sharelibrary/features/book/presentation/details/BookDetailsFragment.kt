package com.szymanski.sharelibrary.features.book.presentation.details

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import kotlinx.android.synthetic.main.fragment_book_details.*
import kotlinx.android.synthetic.main.toolbar_base_with_back_arrow.*
import kotlinx.android.synthetic.main.toolbar_base_with_back_arrow.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BookDetailsFragment : BaseFragment<BookDetailsViewModel>(R.layout.fragment_book_details) {
    override val viewModel: BookDetailsViewModel by viewModel()

    private val TAG = "BookDetailsFragment"

    companion object {
        const val BOOK_DETAILS_KEY = "bookDetailsKey"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<BookDisplayable>(BOOK_DETAILS_KEY)?.let {
            viewModel.setBook(it)
        }
        initAppBar()
        initListeners()
    }

    private fun initAppBar() {
        val toolbar = toolbar_base_with_back_arrow
        (activity as MainActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        toolbar.back_arrow.setOnClickListener {
            viewModel.goBackToBookScreen()
        }
    }

    private fun initListeners() {
        cancel_exchange_button.setOnClickListener {
            viewModel.finishExchange(viewModel.book.value?.id!!)
        }
        display_user_profile_button.setOnClickListener {
            //TODO implement after adding feature display other user profile
        }
        finish_exchange.setOnClickListener {
            viewModel.finishExchange(viewModel.book.value?.id!!)
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.book.observe(this) {
            showBookDetails(it)
        }
        viewModel.bookStatus.observe(this) { status ->
            when (status) {
                BookStatus.AT_OWNER -> {
                    displayAtOwnerStatus()
                }
                BookStatus.DURING_EXCHANGE -> {
                    displayDuringExchangeStatus()
                }
                BookStatus.EXCHANGED -> {
                    displayExchangedStatus()
                }
            }
        }
    }

    private fun displayDuringExchangeStatus() {
        status_during_exchange_wrapper.visibility = View.VISIBLE
        status_exchanged_wrapper.visibility = View.GONE
        user_details_wrapper.visibility = View.GONE

    }

    private fun displayAtOwnerStatus() {
        status_exchanged_wrapper.visibility = View.GONE
        status_during_exchange_wrapper.visibility = View.GONE
        user_details_wrapper.visibility = View.GONE

    }

    private fun displayExchangedStatus() {
        status_exchanged_wrapper.visibility = View.VISIBLE
        user_details_wrapper.visibility = View.VISIBLE
        status_during_exchange_wrapper.visibility = View.GONE
    }

    private fun showBookDetails(bookDisplayable: BookDisplayable) {
        Glide.with(requireActivity())
            .load(bookDisplayable.cover)
            .into(book_details_cover)
        book_details_title.text = bookDisplayable.title?.replace("\"", "")
        bookDisplayable.authorsDisplayable?.let {
            book_details_author.text = convertAuthorDisplayableListToString(it)
        }
        status_value.text = when (bookDisplayable.status) {
            BookStatus.DURING_EXCHANGE -> {
                getString(R.string.book_status_during_exchange)
            }
            BookStatus.EXCHANGED -> {
                getString(R.string.book_status_exchanged)
            }
            else -> {
                getString(R.string.book_status_at_owner)
            }
        }
        user_name_book_details.text = bookDisplayable.atUserDisplayable?.name
        user_surname_book_details.text = bookDisplayable.atUserDisplayable?.surname

    }

    private fun convertAuthorDisplayableListToString(list: List<AuthorDisplayable>): String {
        var endString = ""
        list.forEach { author ->
            endString += "${author.name} ${author.surname}\n"
        }
        endString = endString.trim()
        return endString.substring(0 until endString.length - 1)
    }


}