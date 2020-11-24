package com.szymanski.sharelibrary.features.book.presentation.all

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import kotlinx.android.synthetic.main.item_book.view.*
import java.util.*

class BooksAdapter : RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    private val books: MutableList<BookDisplayable> by lazy { mutableListOf() }

    private lateinit var listener: BooksViewOptions

    private val booksCopy: MutableList<BookDisplayable> by lazy { mutableListOf() }

    fun setBooks(books: List<BookDisplayable>) {
        if (this.books.isNotEmpty()) {
            this.books.clear()
            this.booksCopy.clear()
        }
        this.books.addAll(books)
        this.booksCopy.addAll(books)
        notifyDataSetChanged()
    }

    fun setListener(listener: BooksViewOptions) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(books[position])
    }

    override fun getItemCount() = books.size

    fun filter(newText: String?) {
        books.clear()
        if (newText.isNullOrBlank()) {
            books.addAll(booksCopy)
        } else {
            books.addAll(booksCopy.filter {
                it.title!!.toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))
            })
        }
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View, private val listener: BooksViewOptions) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
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
                item_book_menu_options.setOnClickListener(this@ViewHolder)
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

        override fun onClick(v: View?) {
            listener.onClick(v!!.context, v, adapterPosition)
        }
    }

    interface BooksViewOptions {
        fun onClick(context: Context, view: View, position: Int)
    }
}
