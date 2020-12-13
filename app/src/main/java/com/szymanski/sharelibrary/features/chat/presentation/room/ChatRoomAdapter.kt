package com.szymanski.sharelibrary.features.chat.presentation.room

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.utils.MessageType
import com.szymanski.sharelibrary.features.chat.presentation.model.MessageDisplayable
import kotlinx.android.synthetic.main.item_other_user_message.view.*
import kotlinx.android.synthetic.main.item_user_message.view.*

class ChatRoomAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages: MutableList<MessageDisplayable> = mutableListOf()

    private var userId: Long = -1

    fun setUserId(id: Long) {
        this.userId = id
    }

    fun setMessages(list: List<MessageDisplayable>) {
        if (messages.isNotEmpty()) messages.clear()
        messages.addAll(list)
        notifyDataSetChanged()
        Log.d(TAG, "setMessages: SETTING MESSAGES = ${this.messages.size}")
    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG, "getItemViewType: GETTING ITEM VIEW TYPE")
        return if (messages[position].sender?.id == userId) {
            MessageType.IS_SENT.ordinal
        } else {
            MessageType.IS_RECEIVE.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder: CREATE VIEW HOLDER ")
        return when (viewType) {
            0 -> {//isSent
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user_message, parent, false)
                SendMessageViewHolder(view)
            }
            else -> {//isReceive
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_other_user_message, parent, false)
                ReceiveMessageViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: BIND VIEW HOLDER")
        val message = this.messages[position]
        when (holder) {
            is SendMessageViewHolder -> {
                holder.bind(message)
            }
            is ReceiveMessageViewHolder -> {
                holder.bind(message)
            }
            else -> {
            }
        }
    }

    private val TAG = "ChatRoomAdapter"

    inner class SendMessageViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        @SuppressLint("NewApi")
        fun bind(messageDisplayable: MessageDisplayable) {
            with(view) {
                Log.d(TAG, "bind: User message ")
                val fullName =
                    "${messageDisplayable.sender!!.name} ${messageDisplayable.sender.surname}"
                user_text_message_name.text = fullName
                user_text_message_body.text = messageDisplayable.content
                val date =
                    "${messageDisplayable.timestamp?.toLocalDate()} ${messageDisplayable.timestamp?.hour}:${messageDisplayable.timestamp?.minute}"
                user_text_message_time.text = date
            }
        }
    }

    inner class ReceiveMessageViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        @SuppressLint("SimpleDateFormat", "NewApi")
        fun bind(messageDisplayable: MessageDisplayable) {
            with(view) {
                Log.d(TAG, "bind: Other user message")
                val fullName =
                    "${messageDisplayable.sender!!.name} ${messageDisplayable.sender.surname}"
                other_user_text_message_name.text = fullName
                other_user_text_message_body.text = messageDisplayable.content
                val date =
                    "${messageDisplayable.timestamp?.toLocalDate()} ${messageDisplayable.timestamp?.hour}:${messageDisplayable.timestamp?.minute}"
                other_user_text_message_time.text = date
            }
        }
    }
}