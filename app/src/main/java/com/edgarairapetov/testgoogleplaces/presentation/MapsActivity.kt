package com.edgarairapetov.testgoogleplaces.presentation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.edgarairapetov.testgoogleplaces.R
import com.edgarairapetov.testgoogleplaces.App
import com.edgarairapetov.testgoogleplaces.common.LocationLiveData
import com.edgarairapetov.testgoogleplaces.common.extension.gone
import com.edgarairapetov.testgoogleplaces.common.extension.injectViewModel
import com.edgarairapetov.testgoogleplaces.common.extension.visible
import com.edgarairapetov.testgoogleplaces.common.MapHelper
import com.edgarairapetov.testgoogleplaces.common.MapHelper.Companion.NEEDED_RADIUS
import com.edgarairapetov.testgoogleplaces.common.GpsHelper
import com.edgarairapetov.testgoogleplaces.common.LocationUtils
import com.edgarairapetov.testgoogleplaces.common.OnGpsListener
import com.edgarairapetov.testgoogleplaces.common.RequestCodes
import com.edgarairapetov.testgoogleplaces.domain.MapsViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    OnGpsListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var gpsHelper: GpsHelper

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var map: GoogleMap
    private var mapHelper: MapHelper? = null
    private var currentLocation: Location? = null
    private var checkedTypes = mutableSetOf<String>()
    private var arrayCheckboxes = arrayListOf<AppCompatCheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        App.getComponent().inject(this)
        mapsViewModel = injectViewModel(viewModelFactory)

        progress?.setOnTouchListener { _, _ -> true }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        arrayCheckboxes = arrayListOf(
            cb_type_school, cb_type_florist, cb_type_gas_station, cb_type_cafe
        )

        controlViewsEnabled(false)

        btn_show_places.setOnClickListener {
            currentLocation?.let { location ->
                progress.visible()
                val locStr = location.latitude.toString().plus(",").plus(location.longitude)
                mapsViewModel.places(checkedTypes, locStr, NEEDED_RADIUS.toInt())
            }
        }
    }

    private fun observeViewModel(viewModel: MapsViewModel) {
        viewModel.apply {
            placesLiveData.observe(this@MapsActivity, Observer { places ->
                mapHelper?.apply {
                    clearPreviousPolylines()
                    setPlacesMarker(places)
                    currentLocation?.let { location ->
                        mapsViewModel.route(
                            LatLng(location.latitude, location.longitude),
                            findNearestPlace()
                        )
                    }
                }
            })
            pathLiveData.observe(this@MapsActivity, Observer { direction ->
                direction.forEach { points ->
                    mapHelper?.drawPolyline(points)
                    progress.gone()
                }
            })

            errorLiveData.observe(this@MapsActivity, Observer { error ->
                mapHelper?.apply {
                    clearPreviousPolylines()
                    removePlacesMarkers()
                }
                progress.gone()
                Toast.makeText(this@MapsActivity, error, Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        mapHelper = MapHelper(map)

        if (LocationUtils.isHasPermissions(this)) {
            gpsHelper.turnGpsOn(this, this)
        } else LocationUtils.requestPermission(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, resources.getString(R.string.not_permission), Toast.LENGTH_LONG)
                .show()
            return
        }

        if (requestCode == RequestCodes.LOCATION)
            gpsHelper.turnGpsOn(this, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.GPS && resultCode == Activity.RESULT_OK) {
            gpsHelper.turnGpsOn(this, this)
        }
    }

    override fun gpsStatus(isGPSEnable: Boolean) {
        if (isGPSEnable)
            initLocationLiveData()
    }

    private fun initLocationLiveData() {
        progress.visible()
        val locationLiveData = LocationLiveData(this)
        locationLiveData.observe(this, Observer<Location> {
            if (it == null)
                return@Observer
            locationLiveData.removeObservers(this)
            mapHelper?.setCurrentLocation(it)
            currentLocation = it

            for (checkbox in arrayCheckboxes) {
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked)
                        checkedTypes.add(checkbox.tag.toString())
                    else
                        checkedTypes.remove(checkbox.tag.toString())
                }
            }

            observeViewModel(mapsViewModel)

            controlViewsEnabled(true)

            progress.gone()
        })
    }

    private fun controlViewsEnabled(isEnabled: Boolean) {
        for (checkBox in arrayCheckboxes)
            checkBox.isEnabled = isEnabled

        btn_show_places.isEnabled = isEnabled
    }

}
