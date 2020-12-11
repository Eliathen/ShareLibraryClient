package com.szymanski.sharelibrary.features.chat.presentation.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import kotlinx.android.synthetic.main.item_chat_room.view.*

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val rooms: MutableList<RoomDisplayable> = mutableListOf()

    private var listener: ChatAdapter.Listener? = null

    fun setRooms(rooms: List<RoomDisplayable>) {
        if (this.rooms.isNotEmpty()) this.rooms.clear()
        this.rooms.addAll(rooms)
        notifyDataSetChanged()
    }

    fun setListeners(listener: ChatAdapter.Listener) {
        this.listener = listener;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room, parent, false)
        return ViewHolder(view, listener!!)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
        holder.bind(this.rooms[position])
    }

    override fun getItemCount() = rooms.size

    inner class ViewHolder(private val view: View, private val listener: Listener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {


        fun bind(room: RoomDisplayable) {
            with(view) {
                setOnClickListener(this@ViewHolder)
                val fullName = "${room.recipient?.name} ${room.recipient?.surname}"
                item_chat_room_full_name.text = fullName
                item_chat_room_username.text = room.recipient?.username
            }

        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }
    }

    interface Listener {
        fun onItemClick(position: Int)
    }
}