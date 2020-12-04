package com.szymanski.sharelibrary.features.exchange.all.presentation.mapView

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
import android.os.StrictMode
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.exchange.all.presentation.ExchangesViewModel
import com.szymanski.sharelibrary.features.exchange.all.presentation.model.ExchangeDisplayable
import kotlinx.android.synthetic.main.fragment_exchanges_map_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.wms.BuildConfig


class ExchangesMapViewFragment :
    BaseFragment<ExchangesViewModel>(R.layout.fragment_exchanges_map_view),
    Marker.OnMarkerClickListener {

    override val viewModel: ExchangesViewModel by viewModel()

    private lateinit var map: MapView

    private val REQUEST_PERMISSION_CODE = 101

    private val REQUEST_LOCATION_CODE = 110

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val TAG = "ExchangesMapViewFragment"

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: Location = locationResult.lastLocation!!
            displayUserLocation(latitude = location.latitude, longitude = location.longitude)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val ctx: Context = requireActivity().applicationContext
        Configuration.getInstance()
            .load(ctx, androidx.preference.PreferenceManager.getDefaultSharedPreferences(ctx))
    }

    override fun initViews() {
        super.initViews()
        initMap()
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.exchanges.observe(this) { exchanges ->
            exchanges.forEach {
                displayLocation(it.coordinates.latitude!!, it.coordinates.longitude!!)
            }
        }
    }

    private fun initMap() {
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        map = mapView
        map.setMultiTouchControls(true)
        requestPermissionsIfNecessary(listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ))
        getLastLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        val permissionsToRequest: ArrayList<String?> = ArrayList()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toArray(arrayOfNulls(0)),
                REQUEST_PERMISSION_CODE)
        }
        if (requestCode == REQUEST_LOCATION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun requestPermissionsIfNecessary(permissions: List<String>) {
        val permissionsToRequest: ArrayList<String> = ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toArray(arrayOfNulls(0)),
                REQUEST_PERMISSION_CODE)
        }
    }

    private fun refreshMap(exchanges: List<ExchangeDisplayable>) {
        val mapController = map.controller
        mapController.setZoom(9.5)
        for (exchange in exchanges) {
            val geoPoint =
                GeoPoint(exchange.coordinates.latitude!!, exchange.coordinates.longitude!!)
            val marker = Marker(map)
            marker.position = geoPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)
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
                        displayUserLocation(latitude = location.latitude,
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
            }
            .setNeutralButton(getString(R.string.accept_button_text)) { _: DialogInterface, _: Int ->
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

    private fun displayUserLocation(latitude: Double, longitude: Double) {
        val mapController: IMapController = map.controller
        mapController.setZoom(13.0)
        val startPoint = GeoPoint(latitude, longitude)
        val marker = Marker(map)
        mapController.setCenter(startPoint)
        marker.icon = requireContext().getDrawable(R.drawable.ic_current_location_on_24)
        marker.position = startPoint
        mapView.overlays.add(marker)
        drawCircleAroundPointWithRadius(latitude, longitude, 1000.0)
    }

    private fun displayLocation(latitude: Double, longitude: Double) {
        val point = GeoPoint(latitude, longitude)
        val marker = Marker(map)
        marker.position = point
        marker.icon = requireContext().getDrawable(R.drawable.ic_exchange_location_on_24)
        mapView.overlays.add(marker)
        marker.setOnMarkerClickListener(this)
    }

    private fun drawCircleAroundPointWithRadius(
        latitude: Double,
        longitude: Double,
        radius: Double,
    ) {
        //TO DO Implement drawing circle
    }

    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
        val dialogBuilder = AlertDialog.Builder(requireContext())
//        dialogBuilder.setView(View.inflate(requireContext(), R.layout.))

        return true
    }
}