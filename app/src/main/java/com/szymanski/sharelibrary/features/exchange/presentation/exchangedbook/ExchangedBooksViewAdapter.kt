package com.szymanski.sharelibrary.features.exchange.presentation.exchangedbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.helpers.convertAuthorDisplayableListToString
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import kotlinx.android.synthetic.main.item_exchanges_list_view.view.*

class ExchangedBooksViewAdapter : RecyclerView.Adapter<ExchangedBooksViewAdapter.ViewHolder>() {

    private val books: MutableList<BookDisplayable> = mutableListOf()

    private lateinit var listeners: ExchangedBooksListViewListener

    fun setBooks(books: List<BookDisplayable>) {
        if (this.books.isNotEmpty()) {
            this.books.clear()
        }
        this.books.addAll(books)
        notifyDataSetChanged()
    }

    fun setListeners(listener: ExchangedBooksListViewListener) {
        this.listeners = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ExchangedBooksViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exchanges_list_view, parent, false)
        return ViewHolder(view, listeners)
    }

    override fun onBindViewHolder(holder: ExchangedBooksViewAdapter.ViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size

    inner class ViewHolder(
        private val view: View,
        private val listener: ExchangedBooksListViewListener,
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(bookDisplayable: BookDisplayable) {
            with(view) {
                exchange_distance.visibility = View.GONE
                exchanges_book_title.text = bookDisplayable.title
                exchanges_authors.text =
                    convertAuthorDisplayableListToString(bookDisplayable.authorsDisplayable!!)
            }
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }

    }

    interface ExchangedBooksListViewListener {
        fun onItemClick(position: Int)
    }


}