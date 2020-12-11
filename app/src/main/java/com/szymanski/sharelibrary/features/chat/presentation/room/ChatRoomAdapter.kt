package com.szymanski.sharelibrary.features.chat.presentation.room

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.features.chat.domain.model.Message

class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    private val messages: MutableList<Message> by lazy {
        mutableListOf()
    }

    private var userId: Long = -1

    fun setUserId(id: Long) {
        this.userId = id
    }

    fun setMessages(messages: List<Message>) {
        if (this.messages.isNotEmpty()) this.messages.clear()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount() = messages.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {

        }
    }
}