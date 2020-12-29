package com.szymanski.sharelibrary.features.chat.presentation.room

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import kotlinx.android.synthetic.main.fragment_chat_room.*
import kotlinx.android.synthetic.main.toolbar_base.*
import kotlinx.android.synthetic.main.toolbar_base.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatRoomFragment : BaseFragment<ChatRoomViewModel>(R.layout.fragment_chat_room) {

    override val viewModel: ChatRoomViewModel by viewModel()

    private val chatRoomAdapter: ChatRoomAdapter by inject()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    companion object {
        const val CHAT_ROOM_KEY = "ChatRoomKey"
        const val CHAT_ROOM_OTHER_USER_KEY = "ChatRoomOtherUserKey"

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments?.containsKey(CHAT_ROOM_KEY)!!) {
            arguments?.getParcelable<RoomDisplayable>(CHAT_ROOM_KEY).let {
                viewModel.setRoom(it!!)
            }
        } else {
            arguments?.getParcelable<UserDisplayable>(CHAT_ROOM_OTHER_USER_KEY).let {
                viewModel.setOtherUser(it!!)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initViews() {
        super.initViews()
        initAppBar()
        initRecyclerView()
        viewModel.connectSocket()
        chat_room_send_message_button.setOnClickListener {
            val message = chat_room_message_edit_text.text.toString()
            if (!TextUtils.isEmpty(message)) {
                if (viewModel.messages.value?.size != 0) {
                    viewModel.sendFirstMessage(message)
                } else {
                    viewModel.sendMessage(chat_room_message_edit_text.text.toString())
                }
                chat_room_message_edit_text.text = Editable.Factory.getInstance().newEditable("")
            }
        }
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
            chat_room_recycler_view.scrollToPosition(it.size - 1)
        }
        viewModel.otherUser.observe(this) {
            val fullName = "${it.name} ${it.surname}"
            toolbar_base.toolbar_title.text = fullName
            toolbar_base.toolbar_title.gravity = Gravity.CENTER
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