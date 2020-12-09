package com.szymanski.sharelibrary.features.exchange.presentation.exchangedbook

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.exchange.presentation.listView.ExchangesListViewAdapter
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
        TODO("Not yet implemented")
    }
}