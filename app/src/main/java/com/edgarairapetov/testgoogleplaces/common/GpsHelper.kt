package com.edgarairapetov.testgoogleplaces.common

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.res.Resources
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.edgarairapetov.testgoogleplaces.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient

class GpsHelper(
    val context: Context,
    private val resources: Resources,
    private val settingsClient: SettingsClient,
    private val locationManager: LocationManager
) {
    private val locationSettingsRequest: LocationSettingsRequest

    init {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationUtils.getLocationRequest())
        locationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
    }

    fun turnGpsOn(onGpsListener: OnGpsListener?, activity: Activity) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener?.gpsStatus(true)
        } else {
            settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(activity) {
                    onGpsListener?.gpsStatus(true)
                }
                .addOnFailureListener(activity) { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                activity,
                                RequestCodes.GPS
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i(
                                TAG,
                                resources.getString(R.string.pending_intent_unable_to_execute_request)
                            )
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            displayPromptForEnablingGps()
                        }
                    }
                }
        }
    }

    private fun displayPromptForEnablingGps() {
        val builder = AlertDialog.Builder(context)
        val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        val message = resources.getString(R.string.gps_not_enabled)

        builder.setMessage(message)
            .setPositiveButton(
                resources.getString(R.string.ok)
            ) { d, _ ->
                val intent = Intent(action)
                if (intent.resolveActivity(context.packageManager) != null)
                    context.startActivity(Intent(action))
                d.dismiss()
            }
            .setNegativeButton(
                resources.getString(R.string.cancel)
            ) { d, _ -> d.cancel() }
        builder.create().show()
    }
}
