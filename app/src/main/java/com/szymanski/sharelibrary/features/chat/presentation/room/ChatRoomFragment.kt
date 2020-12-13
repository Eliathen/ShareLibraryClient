package com.szymanski.sharelibrary.features.chat.presentation.room

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_chat_room.*
import kotlinx.android.synthetic.main.toolbar_base.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatRoomFragment : BaseFragment<ChatRoomViewModel>(R.layout.fragment_chat_room) {

    override val viewModel: ChatRoomViewModel by viewModel()

    private val chatRoomAdapter: ChatRoomAdapter by inject()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    companion object {
        const val CHAT_ROOM_KEY = "ChatRoomKey"
    }

    private val TAG = "ChatRoomFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val roomId = arguments?.getLong(CHAT_ROOM_KEY)!!
        viewModel.setRoomId(roomId)
    }

    override fun initViews() {
        super.initViews()
        initAppBar()
        initRecyclerView()
    }

    private fun initAppBar() {
        val toolbar = toolbar_base
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar!!.apply {
            title = ""
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initRecyclerView() {
        chat_room_recycler_view.layoutManager = linearLayoutManager
        chat_room_recycler_view.adapter = chatRoomAdapter
        chatRoomAdapter.setUserId(viewModel.userId)
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.messages.observe(this) {
            chatRoomAdapter.setMessages(it)
        }
    }

    override fun onIdleState() {
        super.onIdleState()
        chat_room_progress_bar.visibility = View.GONE
    }

    override fun onPendingState() {
        super.onPendingState()
        chat_room_progress_bar.visibility = View.VISIBLE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        chat_room_recycler_view.apply {
            layoutManager = null
            adapter = null
        }
    }
}