package com.szymanski.sharelibrary.features.book.presentation.all

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_books.*
import kotlinx.android.synthetic.main.toolbar_books.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class BooksFragment : BaseFragment<BooksViewModel>(R.layout.fragment_books) {

    override val viewModel: BooksViewModel by viewModel()

    lateinit var toolbar: Toolbar

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    private val booksAdapter: BooksAdapter by inject()

    private val TAG = "BooksFragment"

    override fun initViews() {
        super.initViews()
        initAppBar()
        initFabListener()
        initRecyclerView()
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.books.observe(this) {
            booksAdapter.setBooks(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initAppBar() {
        toolbar = books_screen_app_bar
        toolbar.title = getString(R.string.app_name)
        (activity as MainActivity).setSupportActionBar(toolbar)
    }

    private fun initFabListener() {
        save_book_floating_action_button.setOnClickListener {
            viewModel.openSearchBookScreen()
        }
    }

    private fun initRecyclerView() {
        books_recyclerView.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(dividerItemDecoration)
            adapter = booksAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        books_recyclerView.layoutManager = null
        books_recyclerView.adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_books, menu)
        val searchItem = menu.findItem(R.id.search)
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_title)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //TODO implements filter books list
                return true
            }

        })
    }


}