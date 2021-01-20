package com.szymanski.sharelibrary.features.home.presentation.currentexchanges

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.helpers.convertAuthorDisplayableListToString
import com.szymanski.sharelibrary.core.helpers.convertCategoriesDisplayableListToString
import com.szymanski.sharelibrary.core.utils.TAG
import kotlinx.android.synthetic.main.current_exchange_fragment.*
import kotlinx.android.synthetic.main.dialog_current_exchange_details.view.*
import org.koin.android.ext.android.inject

class CurrentExchangeFragment :
    BaseFragment<CurrentExchangeViewModel>(R.layout.current_exchange_fragment),
    CurrentExchangeAdapter.CurrentExchangeAdapterListeners {
    override val viewModel: CurrentExchangeViewModel by inject()

    private val currentExchangeAdapter: CurrentExchangeAdapter by inject()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    override fun initViews() {
        super.initViews()
        current_exchange_recycler_view.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(dividerItemDecoration)
            adapter = currentExchangeAdapter
        }
        Log.d(TAG, "initViews: CurrentExchangeFragment")
        currentExchangeAdapter.setListeners(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: CurrentExchangeFragment")
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.exchanges.observe(this) {
            currentExchangeAdapter.setExchanges(it)
            if (it.isEmpty()) {
                currentExchangeAdapter.notifyItemRemoved(0)
            }
        }
    }

    override fun onIdleState() {
        super.onIdleState()
        current_exchange_progress_bar.visibility = View.GONE

    }

    override fun onPendingState() {
        super.onPendingState()
        current_exchange_progress_bar.visibility = View.VISIBLE
    }

    override fun onExchangeClick(position: Int) {
        val exchange = viewModel.exchanges.value?.get(position)
        val dialogContent = layoutInflater.inflate(R.layout.dialog_current_exchange_details, null)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogContent)
            .setCancelable(true)
        val dialog = alertDialogBuilder.create()
        dialog.show()
        with(dialogContent) {
            current_exchange_cancel_dialog.setOnClickListener {
                dialog.dismiss()
            }
            current_exchange_finish_exchange.setOnClickListener {
                exchange?.id?.let { it1 -> viewModel.finishExchange(it1) }
                dialog.dismiss()
            }
            exchange_icon.setColorFilter(R.color.colorPrimaryDark)
            dialog_current_exchange_details_title.text = exchange?.book?.title
            dialog_current_exchange_details_author.text = exchange?.book?.authorsDisplayable?.let {
                convertAuthorDisplayableListToString(it)
            }
            dialog_current_exchange_details_category.text =
                exchange?.book?.categoriesDisplayable?.let {
                    convertCategoriesDisplayableListToString(it)
                }
            dialog_current_exchange_details_deposit_wrapper.visibility = View.GONE
            if (exchange?.user?.id == viewModel.currentUserId) {
                Log.d(TAG, "onExchangeClick: XDD")
                dialog_current_exchange_details_user_wrapper.visibility = View.GONE
                dialog_current_exchange_second_item_user_name.text = exchange.withUser?.name
                dialog_current_exchange_second_item_user_surname.text = exchange.withUser?.surname
                dialog_current_exchange_chat_imageView2.setOnClickListener {
                    exchange.withUser?.let { user ->
                        viewModel.openChatRoom(user,
                            exchange.user)
                        dialog.dismiss()
                    }
                }
            } else {
                current_exchange_finish_exchange.visibility = View.GONE
                dialog_current_exchange_second_item_user_wrapper.visibility = View.GONE
                dialog_current_exchange_details_user_name.text = exchange?.user?.name
                dialog_current_exchange_details_user_surname.text = exchange?.user?.surname
                dialog_current_exchange_chat_imageView.setOnClickListener {
                    exchange?.withUser?.let { user ->
                        viewModel.openChatRoom(exchange.user,
                            user)
                        dialog.dismiss()
                    }
                }
            }
            if (exchange?.forBook?.id == null) {
                dialog_current_exchange_second_item_deposit_value.text =
                    exchange?.deposit.toString()
                dialog_current_exchange_second_item_title.visibility = View.GONE
                linearLayout3.visibility = View.GONE
            } else {
                dialog_current_exchange_second_item_deposit_wrapper.visibility = View.GONE
                dialog_current_exchange_second_item_title.text = exchange.forBook.title
                dialog_current_exchange_second_item__author.text =
                    exchange.forBook.authorsDisplayable?.let {
                        convertAuthorDisplayableListToString(it)
                    }
                dialog_current_exchange_second_item_category.text =
                    exchange.forBook.categoriesDisplayable?.let {
                        convertCategoriesDisplayableListToString(it)
                    }
            }
        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume: CurrentExchangeFragment")
        viewModel.getCurrentExchanges()
        super.onResume()
    }
}