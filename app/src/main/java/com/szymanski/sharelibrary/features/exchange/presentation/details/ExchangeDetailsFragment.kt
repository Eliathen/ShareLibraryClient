package com.szymanski.sharelibrary.features.exchange.presentation.details

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.helpers.convertAuthorDisplayableListToString
import com.szymanski.sharelibrary.core.helpers.convertCategoriesDisplayableListToString
import com.szymanski.sharelibrary.core.utils.BookCondition
import kotlinx.android.synthetic.main.fragment_exchange_details.*
import kotlinx.android.synthetic.main.toolbar_base.*
import org.koin.android.ext.android.inject

class ExchangeDetailsFragment :
    BaseFragment<ExchangeDetailsViewModel>(R.layout.fragment_exchange_details) {

    override val viewModel: ExchangeDetailsViewModel by inject()

    companion object {
        const val EXCHANGE_DETAILS_KEY = "exchangeDetailsKey"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exchangeId = arguments?.getLong(EXCHANGE_DETAILS_KEY)
        viewModel.getExchangeDetails(exchangeId!!)
    }

    override fun initViews() {
        super.initViews()
        initAppBar()
        initListeners()
    }

    private fun initAppBar() {
        val toolbar = toolbar_base
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.exchange.observe(this) { exchange ->
            exchange_details_title.text = exchange.book.title
            loadImageIfNotNull(exchange.book.cover)

            exchange_details_author.text = exchange.book.authorsDisplayable?.let { authors ->
                convertAuthorDisplayableListToString(authors)
            }
            exchange_details_user_name.text = exchange.user.name
            exchange_details_user_surname.text = exchange.user.surname
            exchange_details_category.text =
                convertCategoriesDisplayableListToString(exchange.book.categoriesDisplayable!!)
            exchange_details_deposit_value.text = exchange.deposit.toString()
            exchange_details_condition.text =
                exchange.book.condition?.let { getTextDependingOnBookCondition(it) }
            exchange_details_language.text = exchange.book.languageDisplayable?.name
        }
        viewModel.requirements.observe(this) { requirements ->
            if (requirements.any { it.user?.id == viewModel.userId }) {
                exchange_details_request_book.visibility = View.GONE
                book_requested_info.visibility = View.VISIBLE
            } else {
                exchange_details_request_book.visibility = View.VISIBLE
                book_requested_info.visibility = View.GONE
            }

        }
    }

    private fun loadImageIfNotNull(cover: ByteArray?) {
        if (cover!!.isNotEmpty()) {
            Glide.with(requireActivity())
                .load(cover)
                .into(exchange_details_cover)
        }
    }

    private fun initListeners() {
        exchange_details_display_profile.setOnClickListener {
            viewModel.openOtherUserScreen()
        }
        exchange_details_request_book.setOnClickListener {
            viewModel.exchange.value?.let { exchange ->
                viewModel.requirementBook(exchange)
                exchange_details_scroll_view.postDelayed({
                    exchange_details_scroll_view.smoothScrollTo(0,
                        exchange_details_scroll_view.height)
                }, 500)
            }
        }
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