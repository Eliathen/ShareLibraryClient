package com.szymanski.sharelibrary.features.exchange.presentation.all

import android.annotation.SuppressLint
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.utils.SortOption
import kotlinx.android.synthetic.main.dialog_bottom_sheet_sort.view.*
import kotlinx.android.synthetic.main.fragment_exchange.*
import kotlinx.android.synthetic.main.toolbar_base.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ExchangesFragment : BaseFragment<ExchangesViewModel>(R.layout.fragment_exchange) {

    override val viewModel: ExchangesViewModel by sharedViewModel()

    private val TAG = "ExchangesFragment"

    override fun initViews() {
        super.initViews()
        initAppBar()
        setViewPager()
    }

    private fun initAppBar() {
        val toolbar = toolbar_base
        (activity as MainActivity).setSupportActionBar(toolbar)
        with((activity as MainActivity).supportActionBar!!) {
            setHasOptionsMenu(true)
            title = ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exchange_menu, menu)
        setSearchView(menu)
    }

    private fun setSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.exchange_searchView)
        val filterItem = menu.findItem(R.id.exchange_filter)

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_title)

        val displayMetrics = requireActivity().resources.displayMetrics
        searchView.maxWidth = displayMetrics.widthPixels - filterItem.icon.intrinsicWidth * 2

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exchange_sort -> {
                displaySortBottomSheet()
                true
            }
            R.id.exchange_filter -> {
                Log.d(TAG, "onOptionsItemSelected: filter")
                true
            }
            else -> {
                false
            }
        }
    }

    private fun setViewPager() {
        val viewPager = exchange_viewPager
        val pagerAdapter = ExchangesViewPagerAdapter(requireActivity())
        viewPager.isUserInputEnabled = false
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(exchangeTabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.icon = when (position) {
                0 -> {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_list_24)
                }
                else -> {
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_map_24)
                }
            }
        }.attach()
    }

    @SuppressLint("InflateParams")
    private fun displaySortBottomSheet() {
        val content = layoutInflater.inflate(R.layout.dialog_bottom_sheet_sort, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(content)
        bottomSheetDialog.show()
        with(content) {
            sort_title_asc.setOnClickListener {
                viewModel.setSort(SortOption.TITLE_ASC)
                bottomSheetDialog.cancel()
            }
            sort_title_desc.setOnClickListener {
                viewModel.setSort(SortOption.TITLE_DESC)
                bottomSheetDialog.cancel()

            }
            sort_distance_asc.setOnClickListener {
                viewModel.setSort(SortOption.DISTANCE_ASC)
                bottomSheetDialog.cancel()

            }
            sort_distance_desc.setOnClickListener {
                viewModel.setSort(SortOption.TITLE_DESC)
                bottomSheetDialog.cancel()

            }
        }
    }

    private fun displayFilterDialog() {

    }
}