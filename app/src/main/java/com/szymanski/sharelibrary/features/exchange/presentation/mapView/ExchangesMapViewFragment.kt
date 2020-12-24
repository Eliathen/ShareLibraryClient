package com.szymanski.sharelibrary.features.exchange.presentation.mapView

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Paint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.utils.TAG
import com.szymanski.sharelibrary.core.utils.defaultRadiusDistance
import com.szymanski.sharelibrary.features.exchange.presentation.all.ExchangesViewModel
import com.szymanski.sharelibrary.features.exchange.presentation.listView.ExchangesListViewAdapter
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import kotlinx.android.synthetic.main.dialog_exchanges.view.*
import kotlinx.android.synthetic.main.fragment_exchanges_map_view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.wms.BuildConfig
import java.io.File


class ExchangesMapViewFragment :
    BaseFragment<ExchangesViewModel>(R.layout.fragment_exchanges_map_view),
    Marker.OnMarkerClickListener {

    override val viewModel: ExchangesViewModel by sharedViewModel()

    private val linearLayoutManager: LinearLayoutManager by inject()

    private val itemDecoration: DividerItemDecoration by inject()

    private var map: MapView? = null

    private val REQUEST_PERMISSION_CODE = 101

    private val REQUEST_LOCATION_CODE = 110

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var dialogContent: View? = null

    private val PREFERENCES_KEY = "ShareLibraryPreferences"

    private val PREFS_TILE_SOURCE = "tilesource"


    private var mPrefs: SharedPreferences? = null

    private var distanceCircle: Polygon? = null

    private var userMarker: Marker? = null

    private var defaultMarker: Marker? = null

    private var exchangeMarker: Marker? = null

    private lateinit var copyrightOverlay: CopyrightOverlay


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: Location = locationResult.lastLocation!!
            displayUserLocation(latitude = location.latitude,
                longitude = location.longitude)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPrefs = requireContext().getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        if (map != null) {
            val ctx: Context = requireActivity().applicationContext
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Configuration.getInstance().apply {
                load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
                userAgentValue = BuildConfig.APPLICATION_ID
                osmdroidTileCache = File(osmdroidBasePath.absolutePath, "tile")
            }
            copyrightOverlay = CopyrightOverlay(activity)
            copyrightOverlay.setTextSize(10)
            map?.overlays?.add(copyrightOverlay)
        }

    }

    override fun initViews() {
        super.initViews()
        initMap()
    }


    override fun initObservers() {
        super.initObservers()
        viewModel.mapOfExchanges.observe(this) { exchanges ->
            val toRemove: Collection<Overlay> =
                map?.overlays?.filter {
                    shouldRemoveOverlay(it)
                }!!
            map?.overlays!!.removeAll(toRemove)
            exchanges.keys.forEach { coordinate ->
                displayLocation(coordinate.latitude!!, coordinate.longitude!!)
            }
        }
        viewModel.user.observe(this)
        {
            displayDefaultLocation(it.coordinates?.latitude!!,
                it.coordinates?.longitude!!
            )
        }
        viewModel.circleRadius.observe(this) { radius ->
            if (radius != defaultRadiusDistance) {
                map?.overlays?.remove(distanceCircle)
                viewModel.currentCoordinates.value?.latitude?.let { latitude ->
                    viewModel.currentCoordinates.value?.longitude?.let { longitude ->
                        drawCircleAroundPointWithRadius(latitude,
                            longitude, radius)
                    }
                }
            } else {
                map?.overlays?.remove(distanceCircle)
                map?.invalidate()
            }
        }
        viewModel.exchangeToDisplay.observe(this) {
            setExchangeMarker(it)
        }
    }

    private fun shouldRemoveOverlay(it: Overlay): Boolean {
        return it != userMarker &&
                it != defaultMarker &&
                it != exchangeMarker &&
                it != copyrightOverlay &&
                it != distanceCircle
    }

    private fun setExchangeMarker(exchange: ExchangeDisplayable) {
        val coordinates = exchange.coordinates
        val point = GeoPoint(coordinates.latitude!!, coordinates.longitude!!)
        exchangeMarker = Marker(map).apply {
            position = point
            icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_default_location_on_24)!!
            setOnMarkerClickListener { _, _ ->
                displayExchangeDetails(exchange)
                true
            }
        }
        map?.overlays?.add(exchangeMarker)
        if (viewModel.user.value?.coordinates == null || viewModel.user.value?.coordinates?.equals(
                coordinates)!!
        ) {
            map?.overlays?.remove(defaultMarker)
        }
        map?.controller?.apply {
            setCenter(point)
            setZoom(13.0)
        }
    }

    @SuppressLint("InflateParams")
    private fun displayExchangeDetails(exchange: ExchangeDisplayable) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogContent = layoutInflater.inflate(R.layout.fragment_exchange_details, null)
        val dialog = builder.setCancelable(true)
            .setTitle(exchange.book.title)
            .create()
        dialog.show()
    }


    private fun initMap() {
        requestPermissionsIfNecessary(listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ))
        map = mapView
        map?.setDestroyMode(false)
        map?.setTileSource(TileSourceFactory.MAPNIK)
        map?.setMultiTouchControls(true)
        getLastLocation()
    }

    override fun onPause() {
        if (map != null) {
            val edit: SharedPreferences.Editor = mPrefs!!.edit()
            edit.putString(PREFS_TILE_SOURCE, map!!.tileProvider.tileSource.name())
            edit.apply()
            map?.onPause()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        val tileSourceName = mPrefs?.getString(PREFS_TILE_SOURCE,
            TileSourceFactory.DEFAULT_TILE_SOURCE.name())
        val tileSource = TileSourceFactory.getTileSource(tileSourceName)
        map?.setTileSource(tileSource)
        map?.onResume()
        requestNewLocationData()
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
                viewModel.navigateBack()
            }
            .setNeutralButton(getString(R.string.accept_button_text)) { _: DialogInterface, _: Int ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                requireActivity().startActivityForResult(intent, REQUEST_LOCATION_CODE)
            }
            .create()
            .show()

    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }

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

    private fun displayDefaultLocation(
        latitude: Double,
        longitude: Double,
    ) {
        val exchangeCoordinates = viewModel.exchangeToDisplay.value?.coordinates
        if (exchangeCoordinates != null
            && exchangeCoordinates.latitude == latitude
            && exchangeCoordinates.longitude == longitude
        ) {
            Log.d(TAG, "displayDefaultLocation: display default location return")
            return
        }
        Log.d(TAG, "displayDefaultLocation: Display default location!!")
        map_view_progress_bar.visibility = View.GONE
        val startPoint = GeoPoint(latitude, longitude)
        defaultMarker = Marker(map).apply {
            title = getString(R.string.default_location)
            icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_default_location_24)!!
            position = startPoint
            setOnMarkerClickListener { marker, _ ->
                if (marker.isInfoWindowOpen) {
                    marker.infoWindow.close()
                } else {
                    marker.showInfoWindow()
                }
                true
            }
        }
        map?.overlays?.add(defaultMarker)
        map?.invalidate()
    }

    private fun displayUserLocation(
        latitude: Double,
        longitude: Double,
    ) {
        if (userMarker != null) {
            map?.overlays?.remove(userMarker)
        }
        map_view_progress_bar.visibility = View.GONE
        val mapController: IMapController = map?.controller!!
        mapController.setZoom(13.0)
        val startPoint = GeoPoint(latitude, longitude)
        userMarker = Marker(map).apply {
            title = getString(R.string.current_location)
            if (viewModel.exchangeToDisplay.value == null) {
                mapController.setCenter(startPoint)
            }
            this.icon = ContextCompat.getDrawable(requireContext(),
                R.drawable.ic_current_location_24)
            position = startPoint
            setOnMarkerClickListener { marker, _ ->
                if (marker.isInfoWindowOpen) {
                    marker.infoWindow.close()
                } else {
                    marker.showInfoWindow()
                }
                true
            }
        }
        map?.overlays?.add(userMarker)
    }


    private fun displayLocation(latitude: Double, longitude: Double) {
        val point = GeoPoint(latitude, longitude)
        val marker = Marker(map)
        marker.position = point
        marker.icon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_exchange_location_24)!!
        map?.overlays?.add(marker)
        marker.setOnMarkerClickListener(this)
    }

    private fun drawCircleAroundPointWithRadius(
        latitude: Double,
        longitude: Double,
        radius: Double,
    ) {
        if (longitude in -180.0..180.0) {
            val circle = Polygon.pointsAsCircle(GeoPoint(latitude, longitude), radius * 1000.0)
            distanceCircle = Polygon(map).apply {
                fillPaint.color =
                    ContextCompat.getColor(requireContext(), R.color.mapCircleInnerColor)
                fillPaint.strokeCap = Paint.Cap.ROUND
                fillPaint.strokeJoin = Paint.Join.ROUND
                outlinePaint.color =
                    ContextCompat.getColor(requireContext(), R.color.mapCircleInnerColor)
                outlinePaint.strokeWidth = 5.0F
                points = circle
                setOnClickListener { _, _, _ -> false }
            }
            map?.overlays?.add(0, distanceCircle)
            map?.invalidate()
        }
    }

    @SuppressLint("InflateParams")
    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
        val key = viewModel.mapOfExchanges.value?.keys?.first {
            it.latitude == marker?.position?.latitude && it.longitude == marker?.position?.longitude
        }
        val exchanges = viewModel.mapOfExchanges.value?.get(key)?.map { ExchangeDisplayable(it) }
        val builder = AlertDialog.Builder(requireContext())
        dialogContent = layoutInflater.inflate(R.layout.dialog_exchanges, null)
        val dialog = builder.setCancelable(false)
            .setView(dialogContent)
            .setNegativeButton("Close", null)
            .create()
        val adapterExchanges = ExchangesListViewAdapter()
        dialogContent!!.dialog_exchanges_recycler_view.apply {
            layoutManager = this@ExchangesMapViewFragment.linearLayoutManager
            addItemDecoration(itemDecoration)
            adapter = adapterExchanges
        }
        adapterExchanges.setListeners(object : ExchangesListViewAdapter.ExchangesListViewListener {
            override fun onItemClick(position: Int) {
                viewModel.displayExchangeDetails(exchanges?.get(position)?.id!!)
                dialogContent!!.dialog_exchanges_recycler_view.apply {
                    layoutManager = null
                    adapter = null
                }
                dialog.dismiss()
            }
        })
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            dialogContent!!.dialog_exchanges_recycler_view.apply {
                layoutManager = null
                adapter = null
            }
            dialog.dismiss()
        }
        exchanges?.let { adapterExchanges.setExchanges(it) }
        dialogContent!!.dialog_exchanges_progress_bar.visibility = View.GONE
        return true
    }

    override fun onStop() {
        super.onStop()
    }
}