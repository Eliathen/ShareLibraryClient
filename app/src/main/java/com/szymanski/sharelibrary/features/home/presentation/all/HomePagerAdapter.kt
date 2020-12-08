package com.szymanski.sharelibrary.features.home.presentation.all

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.szymanski.sharelibrary.features.book.presentation.exchangedbook.ExchangedBookFragment
import com.szymanski.sharelibrary.features.home.presentation.requirements.RequirementsFragment

class HomePagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    private val numberOfFragments = 2
    override fun getItemCount() = numberOfFragments

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RequirementsFragment()
            else -> ExchangedBookFragment()
        }
    }

}