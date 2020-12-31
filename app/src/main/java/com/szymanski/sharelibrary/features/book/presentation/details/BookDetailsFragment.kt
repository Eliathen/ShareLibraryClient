package com.szymanski.sharelibrary.features.book.presentation.details

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.helpers.convertCategoriesDisplayableListToString
import com.szymanski.sharelibrary.core.utils.BookCondition
import com.szymanski.sharelibrary.core.utils.BookStatus
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
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<BookDisplayable>(BOOK_DETAILS_KEY)?.let {
            viewModel.setBook(it)
        }
        initAppBar()
        initListeners()
    }

    private fun initAppBar() {
        val toolbar = toolbar_base
        (activity as MainActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        (activity as MainActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initListeners() {
        cancel_exchange_button.setOnClickListener {
            viewModel.finishExchange(viewModel.book.value?.id!!)
        }
        display_user_profile_button.setOnClickListener {
            viewModel.displayUserProfile()
        }
        finish_exchange.setOnClickListener {
            viewModel.finishExchange(viewModel.book.value?.id!!)
        }
        book_details_show_on_map.setOnClickListener {
            viewModel.displayExchangeOnMap()
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.book.observe(this) {
            showBookDetails(it)
        }
        viewModel.bookStatus.observe(this) { status ->
            when (status) {
                BookStatus.SHARED -> {
                    displayDuringExchangeStatus()
                }
                BookStatus.EXCHANGED -> {
                    displayExchangedStatus()
                }
                else -> {
                    displayAtOwnerStatus()
                }
            }
        }
    }

    private fun displayDuringExchangeStatus() {
        status_during_exchange_wrapper.visibility = View.VISIBLE
        status_exchanged_wrapper.visibility = View.GONE
        user_details_wrapper.visibility = View.GONE
        book_details_show_on_map.visibility = View.VISIBLE

    }

    private fun displayAtOwnerStatus() {
        status_exchanged_wrapper.visibility = View.GONE
        status_during_exchange_wrapper.visibility = View.GONE
        user_details_wrapper.visibility = View.GONE
        book_details_show_on_map.visibility = View.GONE

    }

    private fun displayExchangedStatus() {
        status_exchanged_wrapper.visibility = View.VISIBLE
        user_details_wrapper.visibility = View.VISIBLE
        status_during_exchange_wrapper.visibility = View.GONE
        book_details_show_on_map.visibility = View.VISIBLE
    }

    private fun showBookDetails(bookDisplayable: BookDisplayable) {
        Glide.with(requireActivity())
            .load(bookDisplayable.cover)
            .into(book_details_cover)
        book_details_title.text = bookDisplayable.title?.replace("\"", "")
        bookDisplayable.authorsDisplayable?.let {
            book_details_author.text = convertAuthorDisplayableListToString(it)
        }
        bookDisplayable.categoriesDisplayable?.let {
            book_details_category.text = convertCategoriesDisplayableListToString(it)
        }
        status_value.text = when (bookDisplayable.status) {
            BookStatus.SHARED -> {
                getString(R.string.book_status_during_exchange)
            }
            BookStatus.EXCHANGED -> {
                getString(R.string.book_status_exchanged)
            }
            else -> {
                getString(R.string.book_status_at_owner)
            }
        }
        book_details_language.text = bookDisplayable.languageDisplayable?.name
        book_details_condition.text = getTextDependingOnBookCondition(bookDisplayable.condition)

        user_name_book_details.text = bookDisplayable.atUserDisplayable?.name
        user_surname_book_details.text = bookDisplayable.atUserDisplayable?.surname

    }

    private fun convertAuthorDisplayableListToString(list: List<AuthorDisplayable>): String {
        var endString = ""
        list.forEach { author ->
            endString += "${author.name} ${author.surname}\n"
        }
        endString = endString.trim()
        return endString.substring(endString.indices)
    }

    private fun getTextDependingOnBookCondition(condition: BookCondition): String {
        return when (condition) {
            BookCondition.GOOD -> {
                getString(R.string.book_condition_good)
            }
            BookCondition.NEW -> {
                getString(R.string.book_condition_new)
            }
            else -> {
                getString(R.string.book_condition_bad)
            }
        }
    }


}