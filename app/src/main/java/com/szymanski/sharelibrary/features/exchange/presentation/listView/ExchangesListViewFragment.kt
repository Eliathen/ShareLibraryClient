package com.szymanski.sharelibrary.features.exchange.presentation.listView

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.exchange.presentation.all.ExchangesViewModel
import kotlinx.android.synthetic.main.fragment_exchanges_list_view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ExchangesListViewFragment :
    BaseFragment<ExchangesViewModel>(R.layout.fragment_exchanges_list_view),
    ExchangesListViewAdapter.ExchangesListViewListener {

    override val viewModel: ExchangesViewModel by viewModel()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val itemDecoration: DividerItemDecoration by inject()

    private val exchangesListViewAdapter: ExchangesListViewAdapter by inject()

    private fun initRecyclerView() {
        with(exchanges_list_view) {
            layoutManager = linearLayoutManager
            addItemDecoration(itemDecoration)
            adapter = exchangesListViewAdapter
            exchangesListViewAdapter.setListeners(this@ExchangesListViewFragment)
        }

    }

    override fun initViews() {
        super.initViews()
        initRecyclerView()

    }

    override fun initObservers() {
        super.initObservers()
        viewModel.exchanges.observe(this) {
            exchangesListViewAdapter.setExchanges(it)
        }

    }

    override fun onItemClick(position: Int) {
        viewModel.displayExchangeDetails(viewModel.exchanges.value?.get(position)?.id!!)
    }

    override fun onDestroyView() {
        exchanges_list_view.adapter = null
        exchanges_list_view.layoutManager = null
        super.onDestroyView()
    }
}