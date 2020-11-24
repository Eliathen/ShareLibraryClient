package com.szymanski.sharelibrary.features.book.presentation.searchbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import kotlinx.android.synthetic.main.item_search_book.view.*

class SearchBookAdapter : RecyclerView.Adapter<SearchBookAdapter.ViewHolder>() {

    private val searchResult: MutableList<BookDisplayable> by lazy { mutableListOf() }

    private lateinit var onClickListener: SearchBookOnClickListener

    val usersBooks: MutableList<BookDisplayable> by lazy { mutableListOf() }

    fun setSearchResult(searchResult: List<BookDisplayable>) {
        if (this.searchResult.isNotEmpty()) {
            this.searchResult.clear()
        }
        this.searchResult.addAll(searchResult)
        notifyDataSetChanged()
    }

    fun setUsersBooks(books: List<BookDisplayable>) {
        if (this.usersBooks.isNotEmpty()) {
            this.usersBooks.clear()
        }
        this.usersBooks.addAll(books)
        notifyDataSetChanged()
    }


    fun setListeners(listener: SearchBookOnClickListener) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_book, parent, false)
        return ViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(searchResult[position])
    }

    override fun getItemCount() = searchResult.size

    inner class ViewHolder(
        private val view: View,
        private val onClickListener: SearchBookOnClickListener,
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun onBind(bookDisplayable: BookDisplayable) {
            with(view) {
                title.text = bookDisplayable.title?.replace("\"", "")
                authors.text = bookDisplayable.authorsDisplayable?.let {
                    convertAuthorDisplayableListToString(it)
                }
                if (usersBooks.any { it.id == bookDisplayable.id }) {
                    save_book_imageButton.setImageResource(R.drawable.ic_add_gray_24)
                } else {
                    save_book_imageButton.setOnClickListener(this@ViewHolder)
                }
                save_book_imageButton.setOnClickListener(this@ViewHolder)
                Glide.with(this)
                    .asBitmap()
                    .load(bookDisplayable.cover)
                    .into(cover)
            }
        }

        private fun convertAuthorDisplayableListToString(list: List<AuthorDisplayable>): String {
            var endString = ""
            list.forEach { author ->
                endString += "${author.name} ${author.surname}, "
            }
            return endString.substring(0 until endString.length - 1)
        }

        override fun onClick(v: View?) {
            onClickListener.onClick(adapterPosition)
        }
    }

    interface SearchBookOnClickListener {
        fun onClick(position: Int)
    }
}
