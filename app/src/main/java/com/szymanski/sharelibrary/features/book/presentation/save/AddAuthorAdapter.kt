package com.szymanski.sharelibrary.features.book.presentation.save

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import kotlinx.android.synthetic.main.item_author.view.*

class AddAuthorAdapter : RecyclerView.Adapter<AddAuthorAdapter.ViewHolder>() {

    private val authors: MutableList<AuthorDisplayable> =
        mutableListOf(AuthorDisplayable(name = "", surname = ""))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_author, parent, false)
        return ViewHolder(itemView)
    }

    fun getAuthors(): List<AuthorDisplayable> = authors

    fun setAuthors(authors: List<AuthorDisplayable>) {
        if (this.authors.isNotEmpty()) {
            this.authors.clear()
        }
        this.authors.addAll(authors)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = authors.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            with(itemView) {
                author_name.text =
                    Editable.Factory.getInstance().newEditable(authors[position].name)
                author_surname.text =
                    Editable.Factory.getInstance().newEditable(authors[position].surname)
                this.addAuthorField.setOnClickListener {
                    authors.add(AuthorDisplayable(name = "", surname = ""))
                    notifyDataSetChanged()
                }

                this.author_surname.addTextChangedListener { surname ->
                    authors[position].surname = surname.toString()
                }
                this.author_name.addTextChangedListener { name ->
                    authors[position].name = name.toString()
                }
                if (adapterPosition > 0) {
                    this.addAuthorField.setImageResource(R.drawable.ic_remove_24)
                    this.addAuthorField.setOnClickListener {
                        authors.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
}
