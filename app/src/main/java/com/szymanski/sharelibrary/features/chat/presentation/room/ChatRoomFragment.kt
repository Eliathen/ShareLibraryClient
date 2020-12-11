package com.szymanski.sharelibrary.features.chat.presentation.room

import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_chat_room.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatRoomFragment : BaseFragment<ChatRoomViewModel>(R.layout.fragment_chat_room) {

    override val viewModel: ChatRoomViewModel by viewModel()

    private val chatRoomAdapter: ChatRoomAdapter by inject()

    private val linearLayoutManager: LinearLayoutManager by inject()

    override fun initViews() {
        super.initViews()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        chat_room_recycler_view.apply {
            layoutManager = linearLayoutManager
            adapter = chatRoomAdapter
        }
    }

    override fun initObservers() {
        super.initObservers()
    }

    override fun onIdleState() {
        super.onIdleState()
    }

    override fun onPendingState() {
        super.onPendingState()
    }
}