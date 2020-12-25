package com.szymanski.sharelibrary.features.chat.presentation.all

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_chat.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : BaseFragment<ChatViewModel>(R.layout.fragment_chat), ChatAdapter.Listener {

    override val viewModel: ChatViewModel by viewModel()

    private val chatAdapter: ChatAdapter by inject()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()


    override fun initViews() {
        super.initViews()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        chat_recycler_view.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(dividerItemDecoration)
            adapter = chatAdapter
        }
        chatAdapter.setListeners(this)
        chatAdapter.setUserId(viewModel.userId)
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.rooms.observe(this) {
            chatAdapter.setRooms(it)
        }
    }

    override fun onIdleState() {
        super.onIdleState()
        chat_progress_bar.visibility = View.GONE
    }

    override fun onPendingState() {
        super.onPendingState()
        chat_progress_bar.visibility = View.VISIBLE
    }

    override fun onItemClick(position: Int) {
        viewModel.rooms.value?.get(position)?.let {
            viewModel.openChatRoom(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chat_recycler_view.apply {
            layoutManager = null
            adapter = null
        }
    }
}