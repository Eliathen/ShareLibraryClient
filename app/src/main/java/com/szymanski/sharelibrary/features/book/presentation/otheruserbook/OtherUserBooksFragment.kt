package com.szymanski.sharelibrary.features.book.presentation.otheruserbook

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import kotlinx.android.synthetic.main.dialog_other_user_book_details.view.*
import kotlinx.android.synthetic.main.fragment_other_user_books.*
import kotlinx.android.synthetic.main.toolbar_books.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class OtherUserBooksFragment :
    BaseFragment<OtherUserBooksViewModel>(R.layout.fragment_other_user_books),
    OtherUserBooksAdapter.OtherUserBooksListeners {

    override val viewModel: OtherUserBooksViewModel by viewModel()

    lateinit var toolbar: Toolbar

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    private val otherUserBooksAdapter: OtherUserBooksAdapter by inject()

    private var dialogContent: View? = null

    private val TAG = "OtherUserBooksFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getLong(OTHER_USER_BOOKS_KEY)
        if (id != null) {
            viewModel.getUsersBook(id)
        }
    }

    override fun initViews() {
        super.initViews()
        initAppBar()
        initRecyclerView()

    }

    override fun initObservers() {
        super.initObservers()
        viewModel.books.observe(this) {
            otherUserBooksAdapter.setBooks(it)
        }

    }

    private fun initAppBar() {
        toolbar = books_screen_app_bar
        toolbar.title = getString(R.string.app_name)
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar!!.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initRecyclerView() {
        other_user_books_recyclerView.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(dividerItemDecoration)
            adapter = otherUserBooksAdapter
        }
        otherUserBooksAdapter.setListener(this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        other_user_books_recyclerView.layoutManager = null
        other_user_books_recyclerView.adapter = null
    }

    override fun onIdleState() {
        other_user_books_progressbar.visibility = View.GONE
        if (dialogContent != null) {
            dialogContent!!.dialog_other_user_book_details_progressBar.visibility = View.GONE
            dialogContent!!.dialog_other_user_book_details_wrapper.visibility = View.VISIBLE
        }
    }

    override fun onPendingState() {
        other_user_books_progressbar.visibility = View.VISIBLE
        if (dialogContent != null) {
            dialogContent!!.dialog_other_user_book_details_progressBar.visibility = View.VISIBLE
            dialogContent!!.dialog_other_user_book_details_wrapper.visibility = View.GONE
        }
    }

    override fun onItemClick(context: Context, view: View, position: Int) {
        displayDialogWithBookDetails(viewModel.books.value?.get(position)!!)
        viewModel.getBookRequirements(viewModel.books.value?.get(position)!!)
    }

    private fun displayDialogWithBookDetails(book: BookDisplayable) {
        dialogContent = layoutInflater.inflate(R.layout.dialog_other_user_book_details, null)
        val dialog: AlertDialog = AlertDialog.Builder(requireContext()).setCancelable(true)
            .setView(dialogContent)
            .create()
        with(dialogContent!!) {
            other_user_book_details_title.text = book.title
            if (book.cover!!.isNotEmpty()) {
                Glide.with(this)
                    .load(book.cover)
                    .into(other_user_book_details_cover)
            }
            other_user_book_details_author.text = book.authorsDisplayable?.let {
                convertAuthorDisplayableListToString(it)
            }
            other_user_book_details_request_book.setOnClickListener {
                viewModel.requirementBook()
                dialog.dismiss()
            }
            other_user_book_details_status_value.text = when (book.status) {
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
            viewModel.requirements.observe(this@OtherUserBooksFragment) { list ->
                if (list.any { it.user?.id == viewModel.userId }) {
                    you_requested_book_textView.visibility = View.VISIBLE
                    other_user_book_details_request_book.visibility = View.GONE
                } else {
                    you_requested_book_textView.visibility = View.GONE
                    other_user_book_details_request_book.visibility = View.VISIBLE
                }
            }
        }
        dialog.show()
    }

    private fun convertAuthorDisplayableListToString(list: List<AuthorDisplayable>): String {
        var endString = ""
        list.forEach { author ->
            endString += "${author.name} ${author.surname}\n"
        }
        endString = endString.trim()
        return endString.substring(endString.indices)
    }

    companion object {
        val OTHER_USER_BOOKS_KEY = "otherUserBooksKey"
    }

}