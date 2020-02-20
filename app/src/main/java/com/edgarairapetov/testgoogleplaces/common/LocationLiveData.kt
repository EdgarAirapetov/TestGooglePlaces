package com.edgarairapetov.testgoogleplaces.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.edgarairapetov.testgoogleplaces.utils.LocationUtils.Companion.MAX_LOCATION_ACCURACY
import com.edgarairapetov.testgoogleplaces.utils.LocationUtils.Companion.getLocationRequest
import com.google.android.gms.location.*

class LocationLiveData(appContext: Context) : LiveData<Location>() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    init {
        buildLocationClient(appContext)
    }

    @SuppressLint("MissingPermission")
    @Synchronized
    fun buildLocationClient(appContext: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.forEach {
                    it?.let {
                        if (it.accuracy <= MAX_LOCATION_ACCURACY) {
                            onInactive()
                            value = it
                        }
                    }
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
                if (!p0!!.isLocationAvailable)
                    fusedLocationClient.requestLocationUpdates(
                        getLocationRequest(),
                        locationCallback,
                        null
                    )
            }
        }
        fusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful)
                value = it.result
        }

        fusedLocationClient.requestLocationUpdates(getLocationRequest(), locationCallback, null)
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        fusedLocationClient.requestLocationUpdates(getLocationRequest(), locationCallback, null)
    }

    override fun onInactive() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}