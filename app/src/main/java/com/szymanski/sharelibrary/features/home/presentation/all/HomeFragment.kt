package com.szymanski.sharelibrary.features.home.presentation.all

import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_base.*
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment<HomeViewModel>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by inject()

    override fun initViews() {
        super.initViews()
        initAppBar()
        setViewPager()
    }

    private fun initAppBar() {
        val toolbar = toolbar_base
        (activity as MainActivity).setSupportActionBar(toolbar)
        (activity as MainActivity).supportActionBar.apply {
            title.text = ""
        }
    }

    private fun setViewPager() {
        val viewPager = home_viewPager
        val pagerAdapter = HomePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(home_TabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.icon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_requirements_24)
                }
                else -> {
                    tab.icon = ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_other_users_book_24)
                }
            }
        }.attach()
    }
}