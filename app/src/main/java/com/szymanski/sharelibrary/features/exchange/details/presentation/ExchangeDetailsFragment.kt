package com.szymanski.sharelibrary.features.exchange.details.presentation

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import kotlinx.android.synthetic.main.fragment_exchange_details.*
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

    override fun initObservers() {
        super.initObservers()
        viewModel.exchange.observe(this) {
            exchange_details_title.text = it.book.title
            Glide.with(requireActivity())
                .load(it.book.cover)
                .into(exchange_details_cover)
            exchange_details_author.text = it.book.authorsDisplayable?.let { authors ->
                convertAuthorDisplayableListToString(authors)
            }
            exchange_details_user_name.text = it.user.name
            exchange_details_user_surname.text = it.user.surname
            exchange_details_display_profile.setOnClickListener {
                //TODO navigate to profile with parameter
            }
            exchange_details_request_book.setOnClickListener {
                //TODO send request to api to create request
            }

        }
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