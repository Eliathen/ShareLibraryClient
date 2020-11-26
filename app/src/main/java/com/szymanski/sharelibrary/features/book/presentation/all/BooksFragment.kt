package com.szymanski.sharelibrary.features.book.presentation.all

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
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


class BooksFragment : BaseFragment<BooksViewModel>(R.layout.fragment_books),
    BooksAdapter.BooksViewOptions {

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
        booksAdapter.setListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        books_recyclerView.layoutManager = null
        books_recyclerView.adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.books_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        val searchView = searchItem.actionView as SearchView
        val displayMetrics = requireActivity().resources.displayMetrics
        searchView.maxWidth = displayMetrics.widthPixels
        searchView.queryHint = getString(R.string.search_title)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                booksAdapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                booksAdapter.filter(newText)
                return true
            }

        })
    }

    override fun onClick(context: Context, view: View, position: Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.item_books_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.books_item_remove -> {
                    viewModel.withdrawBook(viewModel.books.value?.get(position))
                    true
                }
                R.id.books_item_share -> {
                    viewModel.shareBook(viewModel.books.value?.get(position))
                    true
                }
                else -> {
                    false
                }
            }
        }
        popupMenu.show()
    }


}