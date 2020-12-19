package com.szymanski.sharelibrary.features.user.presentation.registration

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.gms.location.*
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.user.presentation.model.CoordinateDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment<RegisterViewModel>(R.layout.fragment_register) {
    override val viewModel: RegisterViewModel by viewModel()

    private val TAG = "RegisterFragment"

    private val REQUEST_LOCATION_CODE = 110

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: Location = locationResult.lastLocation!!
            setNewCoordinates(latitude = location.latitude, longitude = location.longitude)
        }
    }

    override fun initViews() {
        super.initViews()
        initListeners()
    }

    private fun initListeners() {
        setSignInTextListener()
        setRegisterButtonListener()
        setCoordinateButtonListener()
        setPasswordFieldTextWatcher()
    }

    private fun setCoordinateButtonListener() {
        coordinates_button.setOnClickListener {
            changeElementsDuringGettingCoordinates()
            getLastLocation()
        }
    }

    private fun setRegisterButtonListener() {
        register_button?.setOnClickListener {
            attemptRegistration()
        }

    }

    override fun initObservers() {
        super.initObservers()
        viewModel.message.observe(this) {
            error_message_wrapper.visibility = View.VISIBLE
            error_message.text = it
        }
        viewModel.coordinate.observe(this) {
            val coordinates = "${it?.latitude},${it?.longitude}"
            coordinates_value_register.text = coordinates
        }
    }

    private fun setPasswordFieldTextWatcher() {
        password_edit_text.doAfterTextChanged {
            if (!isPasswordValid(it.toString())) {
                if (it.toString().isEmpty()) {
                    password_edit_text_wrapper.error = getString(R.string.field_required_error)

                }
                password_edit_text_wrapper.error = getString(R.string.invalid_password_error)
                register_button.isClickable = false
                register_button.isFocusable = false
            } else {
                password_edit_text_wrapper.error = ""
                register_button.isClickable = true
                register_button.isFocusable = true
            }
        }
    }

    private fun attemptRegistration() {
        clearErrors()
        val fullName = full_name_edit_text.text.toString()
        val userName = user_name_edit_text.text.toString()
        val email = email_edit_text.text.toString()
        val password = password_edit_text.text.toString()
        val coordinate = viewModel.coordinate.value
        var cancel = false
        var focusView = View(context)

        if (TextUtils.isEmpty(email)) {
            email_edit_text_wrapper.error = getString(R.string.field_required_error)
            cancel = true
            focusView = email_edit_text
        } else if (isEmailInvalid(email)) {
            email_edit_text_wrapper.error = getString(R.string.invalid_email_error)
            cancel = true
            focusView = email_edit_text
        }
        if (TextUtils.isEmpty(userName)) {
            user_name_edit_text_wrapper.error = getString(R.string.field_required_error)
            cancel = true
            focusView = user_name_edit_text
        }
        if (TextUtils.isEmpty(fullName)) {
            full_name_edit_text_wrapper.error = getString(R.string.field_required_error)
            cancel = true
            focusView = full_name_edit_text
        }
        if (TextUtils.isEmpty(password)) {
            Log.d(TAG, "attemptRegistration: password is empty")
            password_edit_text_wrapper.error = getString(R.string.field_required_error)
            cancel = true
            focusView = password_edit_text
        } else if (!isPasswordValid(password.toString())) {
            Log.d(TAG, "attemptRegistration: password is valid")
            password_edit_text_wrapper.error =
                getString(R.string.invalid_password_error)
            cancel = true
            focusView = password_edit_text
        }
        if (coordinate?.latitude == null || coordinate.longitude == null) {
            coordinates_label_register.error = getString(R.string.field_required_error)
            cancel = true
            focusView = coordinates_label_register
        }

        if (cancel) {
            focusView.requestFocus()
        } else {
            val name: CharSequence
            var surname: CharSequence = ""
            val fullNameTemp = full_name_edit_text.text.toString().trim()
            if (fullNameTemp.contains(' ')) {
                val firstSpace = fullNameTemp.indexOfFirst { it == ' ' }
                name = fullNameTemp.subSequence(0 until firstSpace)
                surname = fullNameTemp.subSequence((firstSpace + 1) until fullNameTemp.length)
            } else {
                name = fullNameTemp
            }
            registerUser(UserDisplayable(
                id = null,
                name = name.toString(),
                surname = surname.toString(),
                username = userName,
                email = email,
                password = password.toCharArray(),
                coordinates = CoordinateDisplayable(
                    null, coordinate?.latitude, coordinate?.longitude
                ),
            ))
        }
    }

    private fun clearErrors() {
        full_name_edit_text_wrapper.error = ""
        user_name_edit_text_wrapper.error = ""
        email_edit_text_wrapper.error = ""
        password_edit_text_wrapper.error = ""
        coordinates_label_register.error = ""
    }

    private fun registerUser(userDisplayable: UserDisplayable) {
        viewModel.registerUser(userDisplayable)
    }

    override fun onIdleState() {
        progress_bar.visibility = View.INVISIBLE
        register_button.visibility = View.VISIBLE
    }

    override fun onPendingState() {
        error_message_wrapper.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
        register_button.visibility = View.INVISIBLE
    }

    private fun isPasswordValid(password: String): Boolean {
        val pattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$".toRegex()
        Log.d(TAG, "isPasswordValid: ${pattern.matches(password)}")
        return pattern.matches(password)
    }

    private fun isEmailInvalid(email: String): Boolean {
        return !email.contains("@")
    }

    private fun setSignInTextListener() {
        signInButtonText?.setOnClickListener {
            viewModel.navigateToLoginScreen()
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
        changeElementsDuringGettingCoordinates()
        val coordinates =
            CoordinateDisplayable(latitude = latitude, longitude = longitude, id = null)
        viewModel.setNewCoordinates(coordinates)
    }

    private fun changeElementsDuringGettingCoordinates() {
        coordinates_label_register.error = null
        if (coordinates_button.visibility == View.VISIBLE) {
            coordinates_button.visibility = View.GONE
            progress_bar_coordinates.visibility = View.VISIBLE
        } else {
            coordinates_button.visibility = View.VISIBLE
            progress_bar_coordinates.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        coordinates_button.performClick()
    }
}