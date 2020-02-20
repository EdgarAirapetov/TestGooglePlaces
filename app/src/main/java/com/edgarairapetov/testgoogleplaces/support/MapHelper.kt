package com.edgarairapetov.testgoogleplaces.support

import android.content.Context
import android.location.Location
import android.util.Log
import com.edgarairapetov.testgoogleplaces.R
import com.edgarairapetov.testgoogleplaces.api.model.place.Result
import com.edgarairapetov.testgoogleplaces.app.App
import com.edgarairapetov.testgoogleplaces.utils.addPlaceMarkerOnMap
import com.edgarairapetov.testgoogleplaces.utils.addPolyline
import com.edgarairapetov.testgoogleplaces.utils.addUserLocMarkerOnMap
import com.edgarairapetov.testgoogleplaces.utils.toBounds
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.maps.android.SphericalUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapHelper(private var map: GoogleMap) {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var disposable: CompositeDisposable

    private var location: Location? = null
    private var userMarker: Marker? = null
    private val placesMarkers = arrayListOf<Marker>()

    companion object {
        const val NEEDED_RADIUS = 3000.0
        const val CAMERA_PADDING = 100
    }

    init {
        App.getComponent().inject(this)
    }

    fun setCurrentLocation(location: Location) {
        this.location = location
        setUserMarker()
    }

    private fun setUserMarker() {
        location?.let {
            if (userMarker == null)
                userMarker = addUserLocMarkerOnMap(map, LatLng(it.latitude, it.longitude))
            else
                userMarker?.position = LatLng(it.latitude, it.longitude)

            calculateRadius()
        }
    }

    private var bounds: LatLngBounds? = null

    private fun calculateRadius() {
        disposable.add(Observable.fromCallable {
            location?.let {
                toBounds(LatLng(it.latitude, it.longitude), NEEDED_RADIUS)
            }
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    bounds = it
                    setupCamera()
                },
                { throwable -> Log.e("ERROR", throwable.message ?: "") }
            )
        )
    }

    private var placesIconMap = mapOf(
        "school" to R.mipmap.ic_school,
        "florist" to R.mipmap.ic_florist,
        "gas_station" to R.mipmap.ic_gas,
        "cafe" to R.mipmap.ic_cafe
    )

    private var placesLatLng = arrayListOf<LatLng>()

    fun removePlacesMarkers() {
        for (placeMarker in placesMarkers)
            placeMarker.remove()

        placesMarkers.clear()

        placesLatLng.clear()
    }

    fun setPlacesMarker(places: ArrayList<Result>) {

        setupCamera()

        removePlacesMarkers()

        for (place in places) {
            place.geometry?.location?.let { location ->
                placesLatLng.add(LatLng(location.lat ?: 0.0, location.lng ?: 0.0))
                placesMarkers.add(
                    addPlaceMarkerOnMap(
                        map, placesLatLng.last(),
                        placesIconMap.getValue(place.type)
                    )
                )
            }
        }
    }

    fun findNearestPlace(): LatLng? {
        location?.let {
            val user = LatLng(location!!.latitude, location!!.longitude)

            return placesLatLng.minBy { point ->
                return@minBy SphericalUtil.computeDistanceBetween(point, user)
            }
        }

        return null
    }


    private fun setupCamera() {
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, CAMERA_PADDING)
        map.animateCamera(cameraUpdate)
    }

    private var polylines = arrayListOf<Polyline>()

    fun clearPreviousPolylines() {
        for (polyline in polylines) {
            polyline.remove()
        }

        polylines.clear()
    }

    fun drawPolyline(points: List<LatLng>) {
        polylines.add(addPolyline(map, points))
    }
}