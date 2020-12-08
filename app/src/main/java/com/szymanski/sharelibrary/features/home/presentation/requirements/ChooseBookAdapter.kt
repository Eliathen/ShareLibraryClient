package com.szymanski.sharelibrary.features.home.presentation.requirements

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.R
import kotlinx.android.synthetic.main.item_dialog_choose_book.view.*

class ChooseBookAdapter : RecyclerView.Adapter<ChooseBookAdapter.ViewHolder>() {

    private val choices: MutableList<String> by lazy {
        mutableListOf()
    }
    private lateinit var listeners: ChooseBookAdapterListeners

    var selectedPosition = 0

    private val TAG = "ChooseBookAdapter"

    fun setChoices(list: List<String>) {
        if (choices.isNotEmpty()) choices.clear()
        choices.addAll(list)
        Log.d(TAG, "setChoices: $choices")
        notifyDataSetChanged()
    }

    fun setListeners(listeners: ChooseBookAdapterListeners) {
        this.listeners = listeners
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dialog_choose_book, parent, false)
        return ViewHolder(view, listeners!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return choices.size
    }

    inner class ViewHolder(
        private val view: View,
        private val listeners: ChooseBookAdapterListeners,
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(position: Int) {
            val choice = choices[position]
            with(view) {
                view.setOnClickListener(this@ViewHolder)
                choose_book_radio_button.isChecked = selectedPosition == position
                choose_book_text_view.text = choice
                choose_book_radio_button.setOnClickListener(this@ViewHolder)
            }
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.choose_book_radio_button -> {
                    selectedPosition = adapterPosition
                    notifyDataSetChanged()
                }
                else -> {
                    listeners.onItemClick(adapterPosition)
                }
            }
        }

    }

    interface ChooseBookAdapterListeners {
        fun onItemClick(position: Int)
//        fun onRadioButtonClick(position: Int)
    }
}