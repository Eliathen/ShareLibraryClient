package com.szymanski.sharelibrary.features.exchange.all.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.szymanski.sharelibrary.features.exchange.all.presentation.listView.ExchangesListViewFragment
import com.szymanski.sharelibrary.features.exchange.all.presentation.mapView.ExchangesMapViewFragment

class ExchangesViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    private val numberOfFragments = 2
    override fun getItemCount() = numberOfFragments

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExchangesMapViewFragment()

            else -> ExchangesListViewFragment()
        }
    }

}