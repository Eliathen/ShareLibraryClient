package com.szymanski.sharelibrary.features.chat.presentation.room

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.utils.MessageType
import com.szymanski.sharelibrary.features.chat.presentation.model.MessageDisplayable
import kotlinx.android.synthetic.main.item_other_user_message.view.*
import kotlinx.android.synthetic.main.item_user_message.view.*
import java.time.LocalDateTime

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
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender?.id == userId) {
            MessageType.IS_SENT.ordinal
        } else {
            MessageType.IS_RECEIVE.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
        when (holder) {
            is SendMessageViewHolder -> {
                holder.bind(position)
            }
            is ReceiveMessageViewHolder -> {
                holder.bind(position)
            }
            else -> {
            }
        }
    }

    inner class SendMessageViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        @SuppressLint("NewApi")
        fun bind(position: Int) {
            val messageDisplayable = messages[position]
            with(view) {
                if (shouldDisplayDate(position)) {
                    val date = messageDisplayable.timestamp?.toLocalDate()
                    val dateToDisplay = "${date?.dayOfMonth}-${date?.monthValue}-${date?.year}"
                    user_message_date.text = dateToDisplay
                } else {
                    user_message_date.visibility = View.GONE
                }
                user_text_message_name.visibility = View.GONE
                user_text_message_body.text = messageDisplayable.content
                user_text_message_time.text = formatDateToString(messageDisplayable.timestamp!!)
            }
        }
    }

    inner class ReceiveMessageViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        @SuppressLint("NewApi")
        fun bind(position: Int) {
            val messageDisplayable = messages[position]
            with(view) {
                if (shouldDisplayDate(position)) {
                    val date = messageDisplayable.timestamp?.toLocalDate()
                    val dateToDisplay = "${date?.dayOfMonth}-${date?.monthValue}-${date?.year}"
                    other_user_message_date.text = dateToDisplay
                } else {
                    this.other_user_message_date.visibility = View.GONE
                }
                val fullName =
                    "${messageDisplayable.sender!!.name} ${messageDisplayable.sender.surname}"
                other_user_text_message_name.text = fullName
                other_user_text_message_body.text = messageDisplayable.content
                other_user_text_message_time.text =
                    formatDateToString(messageDisplayable.timestamp!!)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun shouldDisplayDate(position: Int): Boolean {
        val messageDisplayable = messages[position]
        if (position == 0) {
            return true
        } else if (messageDisplayable.timestamp?.toLocalDate() != messages[position - 1].timestamp?.toLocalDate()) {
            return true
        }
        return false
    }

    @SuppressLint("NewApi")
    private fun formatDateToString(date: LocalDateTime): String {
        if (date.minute < 10) {
            return "${date.hour}:0${date.minute}"
        }
        return "${date.hour}:${date.minute}"
    }
}