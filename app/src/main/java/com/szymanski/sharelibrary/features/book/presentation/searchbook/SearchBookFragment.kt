package com.szymanski.sharelibrary.features.book.presentation.searchbook

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search_book.*
import kotlinx.android.synthetic.main.toolbar_search_books.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchBookFragment : BaseFragment<SearchBookViewModel>(R.layout.fragment_search_book),
    SearchBookAdapter.SearchBookOnClickListener {
    override val viewModel: SearchBookViewModel by viewModel()

    private val searchBooksAdapter: SearchBookAdapter by inject()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    private val TAG = "SearchBookFragment"

    override fun initViews() {
        super.initViews()
        initRecyclerView()
        initAppBar()
        setListeners()
    }

    private fun initAppBar() {
        val toolbar = toolbar_search_books
        (activity as MainActivity).setSupportActionBar(toolbar)

        with((activity as MainActivity).supportActionBar!!) {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHasOptionsMenu(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_books_menu, menu)

        val searchItem = menu.findItem(R.id.search_book)
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_title)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchBook(query!!.trim())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_book -> {
                viewModel.openSaveBookScreen()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.searchBooks.observe(this) {
            if (it.isEmpty()) {
                searchView_wrapper.visibility = View.VISIBLE
                searchBooksAdapter.setSearchResult(listOf())

            } else {
                searchView_wrapper.visibility = View.INVISIBLE
                searchBooksAdapter.setSearchResult(it)
            }
        }
        viewModel.usersBooks.observe(this) {
            searchBooksAdapter.setUsersBooks(it)
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
        searchBooksAdapter.setListeners(this)
    }

    private fun setListeners() {
        add_new_book_button.setOnClickListener {
            viewModel.openSaveBookScreen()
        }
    }

    override fun onIdleState() {
        search_book_progress_bar.visibility = View.GONE
    }

    override fun onPendingState() {
        search_book_progress_bar.visibility = View.VISIBLE
    }

    override fun onClick(position: Int) {
        viewModel.assignBook(viewModel.searchBooks.value?.get(position))
    }
}