package com.szymanski.sharelibrary.features.user.presentation.profile

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
import android.text.Editable
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.base.EditModeState
import com.szymanski.sharelibrary.features.user.presentation.model.CoordinateDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.toolbar_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment<ProfileViewModel>(R.layout.fragment_profile) {
    override val viewModel: ProfileViewModel by viewModel()

    private val REQUEST_LOCATION_CODE = 100

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setListeners()
    }

    override fun initObservers() {
        viewModel.user.observe(this) {
            val fullName = "${it.name} ${it.surname}"
            full_name_profile.text = fullName
        }
        viewModel.coordinate.observe(this) {
            val coordinates = "${it?.latitude} ${it?.longitude}"
            coordinates_value_profile.text = coordinates
        }
        observeEditModeState()
    }

    private fun observeEditModeState() {
        viewModel.editMode.observe(this) {
            when (it) {
                EditModeState.Inactive -> {
                    setInactiveEditModeState()
                }
                EditModeState.Active -> {
                    setActiveEditModeState()
                }
            }
        }
    }

    private fun setListeners() {
        change_details_button.setOnClickListener { viewModel.changeEditModeState() }
        save_details_button.setOnClickListener {
            saveDataChanges()
        }
        coordinates_button_profile.setOnClickListener {
            changeElementsDuringGettingCoordinates()
            getLastLocation()
        }
        cancel_details_changes_button.setOnClickListener {
            viewModel.cancelChanges()
        }
        logout_button.setOnClickListener {
            viewModel.logout()
        }
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

    private fun enableLocation() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
            .setTitle(getString(R.string.location_access_title))
            .setMessage(getString(R.string.get_location_message))
            .setNegativeButton(getString(R.string.cancel_button_text)) { dialog: DialogInterface, _: Int ->
                dialog.cancel()
                changeElementsDuringGettingCoordinates()
            }
            .setNeutralButton(getString(R.string.accept_button_text)) { _: DialogInterface, _: Int ->
                changeElementsDuringGettingCoordinates()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .create()
            .show()
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
        if (requestCode == REQUEST_LOCATION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun setNewCoordinates(latitude: Double, longitude: Double) {
        val coordinates =
            CoordinateDisplayable(latitude = latitude, longitude = longitude, id = null)
        viewModel.setNewCoordinates(coordinates)
        changeElementsDuringGettingCoordinates()
    }

    private fun saveDataChanges() {
        val name: String
        val surname: String
        val fullNameTemp = full_name_editText.text.toString().trim()
        if (fullNameTemp.contains(' ')) {
            val firstSpace = fullNameTemp.indexOfFirst { it == ' ' }
            name = fullNameTemp.substring(0 until firstSpace)
            surname = fullNameTemp.substring((firstSpace + 1) until fullNameTemp.length)
        } else {
            name = fullNameTemp
            surname = ""
        }
        val user = viewModel.user.value
        viewModel.editUserDetails(
            UserDisplayable(name = name,
                surname = surname,
                id = user?.id,
                password = charArrayOf(),
                username = user?.username,
                email = user?.email,
                coordinates = viewModel.coordinate.value
            )
        )
    }

    private fun setToolbar() {
        val toolbar = toolbar_profile_screen
        (activity as MainActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onIdleState() {
        super.onIdleState()
        progress_bar_profile.visibility = View.GONE
        full_name_wrapper.visibility = View.VISIBLE
        linearLayout.visibility = View.VISIBLE

    }

    override fun onPendingState() {
        super.onPendingState()
        progress_bar_profile.visibility = View.VISIBLE
        full_name_wrapper.visibility = View.INVISIBLE
        linearLayout.visibility = View.INVISIBLE

    }

    private fun setActiveEditModeState() {
        full_name_profile.visibility = View.GONE
        full_name_editText.visibility = View.VISIBLE
        coordinates_button_profile.visibility = View.VISIBLE
        save_cancel_buttons_wrapper.visibility = View.VISIBLE
        change_details_button.visibility = View.GONE
        full_name_editText.requestFocus()
        logout_button.visibility = View.GONE
        with(viewModel.user.value) {
            full_name_editText.text =
                Editable.Factory.getInstance().newEditable("${this?.name} ${this?.surname}")
        }
    }

    private fun setInactiveEditModeState() {
        full_name_profile.visibility = View.VISIBLE
        full_name_editText.visibility = View.GONE
        coordinates_button_profile.visibility = View.GONE
        save_cancel_buttons_wrapper.visibility = View.GONE
        change_details_button.visibility = View.VISIBLE
        logout_button.visibility = View.VISIBLE
    }

    private fun changeElementsDuringGettingCoordinates() {
        if (coordinates_button_profile.visibility == View.VISIBLE) {
            coordinates_button_profile.visibility = View.GONE
            progress_bar_coordinates_profile.visibility = View.VISIBLE
        } else {
            coordinates_button_profile.visibility = View.VISIBLE
            progress_bar_coordinates_profile.visibility = View.GONE
        }
    }
}