package com.szymanski.sharelibrary.features.book.presentation.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import kotlinx.android.synthetic.main.item_book.view.*

class BooksAdapter : RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    private val books: MutableList<BookDisplayable> by lazy { mutableListOf() }


    fun setBooks(books: List<BookDisplayable>) {
        if (this.books.isNotEmpty()) {
            this.books.clear()
        }
        this.books.addAll(books)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(books[position])
    }

    override fun getItemCount() = books.size

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(bookDisplayable: BookDisplayable) {
            with(view) {
                title.text = bookDisplayable.title!!.replace("\"", "")
                authors.text = bookDisplayable.authorsDisplayable?.let {
                    convertAuthorDisplayableListToString(it)
                }
                if (bookDisplayable.cover!!.isNotEmpty()) {
                    Glide.with(this)
                        .load(bookDisplayable.cover)
                        .into(cover)
                }
            }
        }

        private fun convertAuthorDisplayableListToString(list: List<AuthorDisplayable>): String {
            var endString = ""
            list.forEach { author ->
                endString += "${author.name} ${author.surname}, "
            }
            endString = endString.trim()
            return endString.substring(0 until endString.length - 1)
        }
    }
}