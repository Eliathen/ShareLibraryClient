package com.szymanski.sharelibrary.features.book.presentation.all

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.CoordinateDisplayable
import kotlinx.android.synthetic.main.dialog_save_exchange.*
import kotlinx.android.synthetic.main.dialog_save_exchange.view.*
import kotlinx.android.synthetic.main.fragment_books.*
import kotlinx.android.synthetic.main.toolbar_books.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class BooksFragment : BaseFragment<BooksViewModel>(R.layout.fragment_books),
    BooksAdapter.BooksAdapterListener {

    override val viewModel: BooksViewModel by viewModel()

    lateinit var toolbar: Toolbar

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val dividerItemDecoration: DividerItemDecoration by inject()

    private val booksAdapter: BooksAdapter by inject()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var coordinateDisplayable: CoordinateDisplayable = CoordinateDisplayable(null, 0.0, 0.0)

    private val REQUEST_LOCATION_CODE: Int = 100

    private val ENABLE_LOCATION_CODE: Int = 101

    private lateinit var dialogContent: View

    private lateinit var alertDialog: AlertDialog

    private var bookId: Long? = null


    override fun initViews() {
        super.initViews()
        initAppBar()
        initRecyclerView()
        initListeners()
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.books.observe(this) {
            booksAdapter.setBooks(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initAppBar() {
        toolbar = books_screen_app_bar
        toolbar.title = getString(R.string.app_name)
        (activity as MainActivity).setSupportActionBar(toolbar)
    }

    private fun initListeners() {
        initFabListener()
        books_swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshBooks()
        }
    }

    private fun initFabListener() {
        save_book_floating_action_button.setOnClickListener {
            viewModel.openSearchBookScreen()
        }
    }

    private fun initRecyclerView() {
        books_recyclerView.apply {
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
            layoutManager = linearLayoutManager
            addItemDecoration(dividerItemDecoration)
            adapter = booksAdapter
        }
        booksAdapter.setListener(this)

    }

    override fun onResume() {
        viewModel.refreshBooks()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        books_recyclerView.layoutManager = null
        books_recyclerView.adapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.books_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        val searchView = searchItem.actionView as SearchView
        val displayMetrics = requireActivity().resources.displayMetrics
        searchView.maxWidth = displayMetrics.widthPixels
        searchView.queryHint = getString(R.string.search_title)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                booksAdapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                booksAdapter.filter(newText)
                return true
            }

        })
    }

    override fun onOptionClick(context: Context, view: View, position: Int) {
        val popupMenu = PopupMenu(context, view)
        when (viewModel.books.value?.get(position)?.status) {
            BookStatus.SHARED -> {
                displayMenuForDuringExchangeStatus(popupMenu, position)
            }
            BookStatus.AT_OWNER -> {
                displayMenuForAtOwnerState(popupMenu, position)
            }
            else -> {
            }
        }
    }

    private fun displayMenuForAtOwnerState(
        popupMenu: PopupMenu,
        position: Int,
    ) {
        popupMenu.inflate(R.menu.item_books_at_owner_state_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.books_item_share -> {
                    viewModel.books.value?.get(position)
                        ?.let { displayDialogForShareBookOption(it) }
                    true
                }
                else -> {
                    false
                }
            }
        }
        popupMenu.show()
    }

    @SuppressLint("InflateParams")
    private fun displayDialogForShareBookOption(bookDisplayable: BookDisplayable) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        dialogContent = layoutInflater.inflate(R.layout.dialog_save_exchange, null)
        alertDialogBuilder.setCancelable(false)
            .setTitle(bookDisplayable.title)
            .setView(dialogContent)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("Share", null)
        alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        dialogContent.location_radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.current_location_checkbox -> {
                    dialogContent.save_exchange_progressbar.visibility = View.VISIBLE
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isClickable = false
                    bookId = bookDisplayable.id
                    getLastLocation()
                }
            }
        }
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val deposit = dialogContent.deposit_value.text.toString()
            if (TextUtils.isEmpty(deposit)) {
                dialogContent.deposit_value.error = getString(R.string.field_required_error)
                dialogContent.deposit_value.requestFocus()
            } else {
                if (dialogContent.default_location_checkbox.isChecked) {
                    viewModel.shareBook(bookDisplayable, deposit.toDouble())
                } else {
                    viewModel.shareBook(bookDisplayable, deposit.toDouble(), coordinateDisplayable)
                }
                alertDialog.dismiss()
            }
        }
    }

    private fun displayMenuForDuringExchangeStatus(
        popupMenu: PopupMenu,
        position: Int,
    ) {
        //TODO create new menu for duringExchangeStatus
        popupMenu.inflate(R.menu.item_books_exchange_state_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.books_item_cancel_exchange -> {
                    viewModel.finishExchange(viewModel.books.value!![position])
                    true
                }
                else -> {
                    false
                }
            }
        }
        popupMenu.show()
    }

    override fun onItemClick(context: Context, view: View, position: Int) {
        viewModel.openBookDetailsScreen(viewModel.books.value!![position])
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
                        setNewCoordinates(latitude = location.latitude,
                            longitude = location.longitude)
                    }
                }
            } else {
                enableLocation()
            }
        } else {
            requestPermissions()
        }
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

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.isEmpty() || grantResults[0] == PermissionChecker.PERMISSION_DENIED) {
                requireActivity().finish()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun enableLocation() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
            .setTitle(getString(R.string.location_access_title))
            .setMessage(getString(R.string.get_location_message))
            .setNegativeButton(getString(R.string.cancel_button_text)) { dialog: DialogInterface, _: Int ->
                dialog.cancel()
                requireActivity().finish()
            }
            .setNeutralButton(getString(R.string.accept_button_text)) { _: DialogInterface, _: Int ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, ENABLE_LOCATION_CODE)
            }.create().show()
        alertDialog.dismiss()
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

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: Location = locationResult.lastLocation!!
            setNewCoordinates(latitude = location.latitude, longitude = location.longitude)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ENABLE_LOCATION_CODE) {
            displayDialogForShareBookOption(viewModel.books.value!!.first { it.id == bookId })
        }
    }

    private fun setNewCoordinates(latitude: Double, longitude: Double) {
        coordinateDisplayable =
            CoordinateDisplayable(latitude = latitude, longitude = longitude, id = null)
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isClickable = true
        alertDialog.save_exchange_progressbar.visibility = View.GONE
    }

    override fun onIdleState() {
        books_swipeRefreshLayout.isRefreshing = false
    }

    private val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                displayDialogToRemoveBook(viewHolder.adapterPosition)
            }

        }

    private fun displayDialogToRemoveBook(position: Int) {
        val book = viewModel.books.value!![position]
        val builder = AlertDialog.Builder(requireContext())
        if (book.status == BookStatus.EXCHANGED) {
            booksAdapter.notifyItemChanged(position)
            builder.setCancelable(false)
                .setTitle("Warning")
                .setMessage("You cannot removed exchanged book")
                .setNeutralButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create().show()
            return
        }
        builder.setCancelable(false)
            .setTitle(book.title)
            .setMessage(R.string.remove_book_message)
            .setNegativeButton(R.string.cancel_changes) { dialog, _ ->
                booksAdapter.notifyItemChanged(position)
                dialog.dismiss()
            }
            .setPositiveButton(R.string.accept_button_text) { _, _ ->
                viewModel.withdrawBook(book)
            }
            .create().show()
    }

}