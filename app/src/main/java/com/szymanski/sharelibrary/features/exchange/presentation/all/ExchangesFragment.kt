package com.szymanski.sharelibrary.features.exchange.presentation.all

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.utils.SortOption
import com.szymanski.sharelibrary.core.utils.defaultRadiusDistance
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import kotlinx.android.synthetic.main.dialog_bottom_sheet_sort.view.*
import kotlinx.android.synthetic.main.dialog_filters.view.*
import kotlinx.android.synthetic.main.fragment_exchange.*
import kotlinx.android.synthetic.main.toolbar_base.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ExchangesFragment : BaseFragment<ExchangesViewModel>(R.layout.fragment_exchange) {

    override val viewModel: ExchangesViewModel by sharedViewModel()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val REQUEST_LOCATION_CODE = 101

    private lateinit var pagerAdapter: ExchangesViewPagerAdapter

    private lateinit var viewPager: ViewPager2

    companion object {
        const val EXCHANGE_KEY = "ExchangeToDisplayKey"
    }

    override fun initViews() {
        super.initViews()
        initAppBar()
        setViewPager()
        getLastLocation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null && arguments?.containsKey(EXCHANGE_KEY)!!) {
            val exchange = arguments?.get(EXCHANGE_KEY) as ExchangeDisplayable
            viewModel.setExchangeToDisplay(exchange)
            viewPager.postDelayed({ viewPager.currentItem = 1 }, 10)
        }

    }

    private fun initAppBar() {
        val toolbar = toolbar_base
        (activity as MainActivity).setSupportActionBar(toolbar)
        with((activity as MainActivity).supportActionBar!!) {
            setHasOptionsMenu(true)
            title = ""
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.currentCoordinates.observe(this) {
            viewModel.getFilteredExchanges()
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
                viewModel.setQuery(query!!)
                viewModel.getFilteredExchanges()
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
                displayFilterDialog()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun setViewPager() {
        viewPager = exchange_viewPager
        pagerAdapter = ExchangesViewPagerAdapter(requireActivity())
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
                viewModel.setSort(SortOption.DISTANCE_DESC)
                bottomSheetDialog.cancel()
            }
        }
    }

    private fun displayFilterDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogContent = layoutInflater.inflate(R.layout.dialog_filters, null)
        builder.setCancelable(true)
            .setView(dialogContent)
        val dialog = builder.create()
        with(dialogContent) {
            val chipGroup = dialog_filters_chip_group
            viewModel.listOfCategories.value?.forEachIndexed { _, item ->
                val newChip =
                    layoutInflater.inflate(R.layout.chip_style_filter, chipGroup, false) as Chip
                newChip.text = item.name
                if (viewModel.getChosenCategories()?.containsKey(item.name)!!) {
                    newChip.isChecked = viewModel.getChosenCategories()?.get(item.name)!!
                }
                chipGroup.addView(newChip as View)
                newChip.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.setChosenCategory(item.name, isChecked)
                }
            }
            dialog_filters_cancel_button.setOnClickListener { dialog.dismiss() }
            dialog_filters_filter_button.setOnClickListener {
                if (viewModel.getRadius() == defaultRadiusDistance) viewModel.setRadius(1.0)
                viewModel.getFilteredExchanges()
                dialog.dismiss()
            }
            dialog_filters_reset_button.setOnClickListener {
                viewModel.resetFilters()
                chipGroup.forEach {
                    if (it is Chip) {
                        it.isChecked = false
                    }
                }
                dialog_filters_distance_picker.value = viewModel.getRadius()?.toInt()!!
                viewModel.getFilteredExchanges()
            }
            dialog_filters_distance_picker.apply {
                maxValue = 99
                minValue = 1
                value = viewModel.getRadius()?.toInt()!!
                setOnValueChangedListener { _, _, newVal ->
                    viewModel.setRadius(newVal.toDouble())
                }
            }
        }
        dialog.show()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        setUserCoordinates(location.latitude, location.longitude)
                    }
                }
            } else {
                enableLocation()
            }
        } else {
            requestPermissions()
        }
    }

    private fun setUserCoordinates(latitude: Double, longitude: Double) {
        viewModel.setCoordinates(latitude, longitude)
    }

    private fun enableLocation() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
            .setTitle(getString(R.string.location_access_title))
            .setMessage(getString(R.string.get_location_message))
            .setNegativeButton(getString(R.string.cancel_button_text)) { dialog: DialogInterface, _: Int ->
                dialog.cancel()
                viewModel.navigateBack()
            }
            .setNeutralButton(getString(R.string.accept_button_text)) { _: DialogInterface, _: Int ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                requireActivity().startActivityForResult(intent, REQUEST_LOCATION_CODE)
            }
            .create()
            .show()

    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            val location: Location = result?.lastLocation!!
            setUserCoordinates(location.latitude, location.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!isLocationEnabled()) {
            viewModel.navigateBack()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_CODE
        )
    }
}