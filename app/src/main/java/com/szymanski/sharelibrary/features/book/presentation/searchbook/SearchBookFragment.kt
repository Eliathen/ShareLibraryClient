package com.szymanski.sharelibrary.features.book.presentation.searchbook

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search_book.*
import kotlinx.android.synthetic.main.toolbar_base.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchBookFragment : BaseFragment<SearchBookViewModel>(R.layout.fragment_search_book) {
    override val viewModel: SearchBookViewModel by viewModel()

    private val searchBooksAdapter: SearchBookAdapter by inject()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    override fun initViews() {
        super.initViews()
        initRecyclerView()
        initAppBar()
        initSearchView()
        setListeners()
    }

    private fun initAppBar() {
        val toolbar = base_toolbar
        (activity as MainActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        (activity as MainActivity).supportActionBar!!.setDisplayShowHomeEnabled(true);
    }

    private fun initSearchView() {
        books_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchBook(query!!.trim())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.searchBooks.observe(this) {
            if (it.isEmpty()) {
                searchView_wrapper.visibility = View.VISIBLE

            } else {
                searchView_wrapper.visibility = View.INVISIBLE
                searchBooksAdapter.setBooks(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        search_books_recyclerView.adapter = null
        search_books_recyclerView.layoutManager = null
    }

    private fun initRecyclerView() {
        search_books_recyclerView.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(dividerItemDecoration)
            adapter = searchBooksAdapter
        }
    }

    private fun setListeners() {
        add_new_book_button.setOnClickListener {
            viewModel.openSaveBookScreen()
        }
    }

    override fun onIdleState() {
        progress_bar.visibility = View.GONE
    }

    override fun onPendingState() {
        progress_bar.visibility = View.VISIBLE

    }
}