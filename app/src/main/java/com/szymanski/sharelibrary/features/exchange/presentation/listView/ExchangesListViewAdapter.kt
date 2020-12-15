package com.szymanski.sharelibrary.features.exchange.presentation.listView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.helpers.convertAuthorDisplayableListToString
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import kotlinx.android.synthetic.main.item_exchanges_list_view.view.*

class ExchangesListViewAdapter : RecyclerView.Adapter<ExchangesListViewAdapter.ViewHolder>() {

    private val exchanges: MutableList<ExchangeDisplayable> = mutableListOf()

    private lateinit var listeners: ExchangesListViewListener

    fun setExchanges(exchanges: List<ExchangeDisplayable>) {
        if (this.exchanges.isNotEmpty()) {
            this.exchanges.clear()
        }
        this.exchanges.addAll(exchanges)
        notifyDataSetChanged()
    }

    fun setListeners(listener: ExchangesListViewListener) {
        this.listeners = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ExchangesListViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exchanges_list_view, parent, false)
        return ViewHolder(view, listeners)
    }

    override fun onBindViewHolder(holder: ExchangesListViewAdapter.ViewHolder, position: Int) {
        holder.bind(exchanges[position])
    }

    override fun getItemCount() = exchanges.size

    inner class ViewHolder(
        private val view: View,
        private val listener: ExchangesListViewListener,
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(exchangeDisplayable: ExchangeDisplayable) {
            val distance = exchangeDisplayable.distance
            with(view) {
                if (distance != null) {
                    exchange_distance.text = if (distance < 1.0) {
                        "${distance / 1000} m"
                    } else {
                        "$distance km"
                    }
                }
                exchanges_book_title.text = exchangeDisplayable.book.title
                exchanges_authors.text =
                    convertAuthorDisplayableListToString(exchangeDisplayable.book.authorsDisplayable!!)
            }
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }

    }

    interface ExchangesListViewListener {
        fun onItemClick(position: Int)
    }


}