package com.szymanski.sharelibrary.features.book.presentation.otheruserbook

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.helpers.convertAuthorDisplayableListToString
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import kotlinx.android.synthetic.main.item_book.view.*

class OtherUserBooksAdapter : RecyclerView.Adapter<OtherUserBooksAdapter.ViewHolder>() {

    private val books: MutableList<BookDisplayable> by lazy { mutableListOf() }

    private lateinit var optionListener: OtherUserBooksListeners


    fun setBooks(books: List<BookDisplayable>) {
        if (this.books.isNotEmpty()) {
            this.books.clear()
        }
        this.books.addAll(books)
        notifyDataSetChanged()
    }

    fun setListener(listener: OtherUserBooksListeners) {
        this.optionListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(view, optionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(books[position])
    }

    override fun getItemCount() = books.size

    class ViewHolder(private val view: View, private val listener: OtherUserBooksListeners) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        fun onBind(bookDisplayable: BookDisplayable) {
            with(view) {
                toolbar_title.text = bookDisplayable.title!!.replace("\"", "")
                bookDisplayable.authorsDisplayable?.let {
                    item_book_authors.text = convertAuthorDisplayableListToString(it)
                }
                bookDisplayable.status?.let {
                    book_status.text = getStringByBookStatus(it)
                }
                if (bookDisplayable.cover!!.isNotEmpty()) {
                    Glide.with(this)
                        .load(bookDisplayable.cover)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean,
                            ): Boolean {
                                item_book_progress_bar.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean,
                            ): Boolean {
                                item_book_progress_bar.visibility = View.GONE
                                return false
                            }
                        })
                        .into(cover)
                }
                setOnClickListener(this@ViewHolder)
                item_book_menu_options.visibility = View.GONE
            }
        }

        private fun getStringByBookStatus(status: BookStatus): String {
            return when (status) {
                BookStatus.EXCHANGED -> view.context.resources.getString(R.string.book_status_exchanged)
                BookStatus.SHARED -> view.context.resources.getString(R.string.book_status_during_exchange)
                else -> ""
            }
        }

        override fun onClick(v: View?) {
            listener.onItemClick(v!!.context, v, adapterPosition)
        }
    }

    interface OtherUserBooksListeners {
        fun onItemClick(context: Context, view: View, position: Int)
    }
}
