package com.szymanski.sharelibrary.features.home.presentation.currentexchanges

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.features.home.presentation.model.ExchangeDetailsDisplayable
import kotlinx.android.synthetic.main.item_current_exchange.view.*

class CurrentExchangeAdapter : RecyclerView.Adapter<CurrentExchangeAdapter.ViewHolder>() {

    private val exchanges: MutableList<ExchangeDetailsDisplayable> = mutableListOf()

    private lateinit var listeners: CurrentExchangeAdapterListeners

    fun setExchanges(exchanges: List<ExchangeDetailsDisplayable>) {
        if (this.exchanges.isNotEmpty()) {
            this.exchanges.clear()
        }
        this.exchanges.addAll(exchanges)
        notifyDataSetChanged()
    }

    fun setListeners(listeners: CurrentExchangeAdapterListeners) {
        this.listeners = listeners
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_current_exchange, parent, false)
        return ViewHolder(view, listeners)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exchanges[position])
    }

    override fun getItemCount(): Int {
        return exchanges.size
    }

    inner class ViewHolder(
        private val view: View,
        private val listeners: CurrentExchangeAdapterListeners,
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(exchangeDisplayable: ExchangeDetailsDisplayable) {
            with(view) {
                current_exchange_first_item.text = exchangeDisplayable.book.title
                current_exchange_second_item.text =
                    if (exchangeDisplayable.forBook?.title != null) {
                        exchangeDisplayable.forBook.title
                    } else {
                        "${exchangeDisplayable.deposit} zł"
                    }
                icon_exchange.setColorFilter(R.color.colorPrimaryDark)
                view.setOnClickListener(this@ViewHolder)
            }
        }

        override fun onClick(v: View?) {
            listeners.onExchangeClick(adapterPosition)
        }

    }

    interface CurrentExchangeAdapterListeners {
        fun onExchangeClick(position: Int)
    }
}
