package com.szymanski.sharelibrary.features.home.presentation.requirements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.features.home.presentation.model.RequirementDisplayable
import com.szymanski.sharelibrary.features.home.presentation.requirements.RequirementsAdapter.ViewHolder
import kotlinx.android.synthetic.main.item_requirement.view.*

class RequirementsAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val requirements: MutableList<RequirementDisplayable> by lazy {
        mutableListOf()
    }

    private var listener: Listeners? = null

    fun setRequirements(list: List<RequirementDisplayable>) {
        if (requirements.isNotEmpty()) requirements.clear()
        requirements.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: Listeners) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_requirement, null)
        return ViewHolder(view, listener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(requirements[position])
    }

    override fun getItemCount() = requirements.size

    class ViewHolder(private val view: View, private val listener: Listeners) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        fun bind(requirement: RequirementDisplayable) {
            with(view) {
                item_requirement_title.text = requirement.exchange?.book?.title
                val fullName = "${requirement.user?.name} ${requirement.user?.surname}"
                item_requirement_full_name.text = fullName
                view.setOnClickListener(this@ViewHolder)
            }
        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }

    }

    interface Listeners {

        fun onItemClick(position: Int)

    }
}