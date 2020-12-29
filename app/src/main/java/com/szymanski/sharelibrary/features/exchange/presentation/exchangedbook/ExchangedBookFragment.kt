package com.szymanski.sharelibrary.features.exchange.presentation.exchangedbook

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.helpers.convertCategoriesDisplayableListToString
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.exchange.presentation.listView.ExchangesListViewAdapter
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import kotlinx.android.synthetic.main.dialog_other_user_book_details.view.*
import kotlinx.android.synthetic.main.fragment_exchanged_book.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangedBookFragment :
    BaseFragment<ExchangedBookViewModel>(R.layout.fragment_exchanged_book),
    ExchangesListViewAdapter.ExchangesListViewListener {

    override val viewModel: ExchangedBookViewModel by viewModel()

    private val adapter: ExchangesListViewAdapter by inject()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val decorator: DividerItemDecoration by inject()

    override fun initViews() {
        super.initViews()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        exchanged_book_recycler_view.apply {
            layoutManager = this@ExchangedBookFragment.linearLayoutManager
            addItemDecoration(decorator)
            adapter = this@ExchangedBookFragment.adapter
        }
        adapter.setListeners(this)
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.exchanges.observe(this) {
            adapter.setExchanges(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        with(exchanged_book_recycler_view) {
            layoutManager = null
            adapter = null
        }
    }

    override fun onIdleState() {
        super.onIdleState()
        exchanged_book_progress_bar.visibility = View.GONE
    }

    override fun onPendingState() {
        super.onPendingState()
        exchanged_book_progress_bar.visibility = View.VISIBLE
    }

    override fun onItemClick(position: Int) {
        viewModel.downloadCover(viewModel.exchanges.value?.get(position)?.book!!)
        viewModel.book.observe(this) {
            viewModel.exchanges.value?.get(position)?.let { displayBookDetails(it) }
        }
    }

    private fun displayBookDetails(exchange: ExchangeDisplayable) {
        val book = exchange.book
        val content = layoutInflater.inflate(R.layout.dialog_other_user_book_details, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(content)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.create()
        dialog.show()
        with(content!!) {
            other_user_book_details_title.text = book.title
            if (book.cover!!.isNotEmpty()) {
                Glide.with(this)
                    .load(book.cover)
                    .into(other_user_book_details_cover)
            }
            other_user_book_details_author.text = book.authorsDisplayable?.let {
                convertAuthorDisplayableListToString(it)
            }
            other_user_book_category.text = book.categoriesDisplayable?.let {
                convertCategoriesDisplayableListToString(it)
            }
            dialog_other_user_details_label.text = context.getString(R.string.owner_label)
            dialog_other_user_details_wrapper.visibility = View.VISIBLE
            val fullName = "${exchange.user.name} ${exchange.user.surname}"
            dialog_other_user_details_full_name.text = fullName
            dialog_other_user_book_details_status_wrapper.visibility = View.GONE
            dialog_other_user_send_message_button.setOnClickListener {
                dialog.dismiss()
                exchange.user.let { viewModel.openChatRoom(it) }
            }
        }
    }

    private fun convertAuthorDisplayableListToString(list: List<AuthorDisplayable>): String {
        var endString = ""
        list.forEach { author ->
            endString += "${author.name} ${author.surname}\n"
        }
        endString = endString.trim()
        return endString.substring(endString.indices)
    }
}