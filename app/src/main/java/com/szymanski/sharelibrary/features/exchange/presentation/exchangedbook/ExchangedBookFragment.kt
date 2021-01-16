package com.szymanski.sharelibrary.features.exchange.presentation.exchangedbook

import android.app.AlertDialog
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.helpers.convertCategoriesDisplayableListToString
import com.szymanski.sharelibrary.core.utils.BookCondition
import com.szymanski.sharelibrary.core.utils.TAG
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import kotlinx.android.synthetic.main.dialog_other_user_book_details.view.*
import kotlinx.android.synthetic.main.fragment_exchanged_book.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangedBookFragment :
    BaseFragment<ExchangedBookViewModel>(R.layout.fragment_exchanged_book),
    ExchangedBooksViewAdapter.ExchangedBooksListViewListener {

    override val viewModel: ExchangedBookViewModel by viewModel()

    private val adapter: ExchangedBooksViewAdapter by inject()

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
        viewModel.books.observe(this) {
            adapter.setBooks(it)
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
        viewModel.downloadCover(viewModel.books.value?.get(position)!!)
        viewModel.book.observe(this) {
            viewModel.books.value?.get(position)?.let { displayBookDetails(it) }
        }
    }

    private fun displayBookDetails(book: BookDisplayable) {
        Log.d(TAG, "displayBookDetails: ")
        val user: UserDisplayable? = viewModel.getUserByBook(book)
        Log.d(TAG, "displayBookDetails: $user")
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
            other_user_book_condition.text = book.condition?.let {
                getTextDependingOnBookCondition(it)
            }
            other_user_book_language.text = book.languageDisplayable?.name
            dialog_other_user_details_label.text = context.getString(R.string.owner_label)
            dialog_other_user_details_wrapper.visibility = View.VISIBLE
            val fullName = "${user?.name} ${user?.surname}"
            dialog_other_user_details_full_name.text = fullName
            dialog_other_user_book_details_status_wrapper.visibility = View.GONE
            dialog_other_user_send_message_button.setOnClickListener {
                dialog.dismiss()
                user.let {
                    if (it != null) {
                        viewModel.openChatRoom(it)
                    }
                }
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