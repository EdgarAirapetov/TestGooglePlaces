package com.edgarairapetov.testgoogleplaces.common

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.SphericalUtil
import kotlin.math.sqrt

const val USER_MARKER_ELEVATION = 3f
const val PLACES_MARKER_ELEVATION = 2f
const val POLYLINE_WIDTH = 10f
const val PATTERN_DASH_LENGTH_PX = 30
const val PATTERN_GAP_LENGTH_PX = 10
val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
val PATTERN_POLYLINE_ALPHA = listOf(DASH, GAP)

fun addUserLocMarkerOnMap(map: GoogleMap, latLng: LatLng): Marker {
    return map.addMarker(
        MarkerOptions()
            .zIndex(USER_MARKER_ELEVATION)
            .position(LatLng(latLng.latitude, latLng.longitude))
    )
}

fun addPlaceMarkerOnMap(map: GoogleMap, latLng: LatLng, icon: Int): Marker {
    return map.addMarker(
        MarkerOptions()
            .zIndex(PLACES_MARKER_ELEVATION)
            .position(LatLng(latLng.latitude, latLng.longitude))
            .icon(BitmapDescriptorFactory.fromResource(icon))
    )
}

fun toBounds(center: LatLng, radiusInMeters: Double): LatLngBounds {
    val distanceFromCenterToCorner = radiusInMeters * sqrt(2.0)
    val southwestCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0)
    val northeastCorner = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0)
    return LatLngBounds(southwestCorner, northeastCorner)
}

fun addPolyline(map: GoogleMap, points: List<LatLng>): Polyline {
    return map.addPolyline(
        PolylineOptions().addAll(points)
            .color(Color.BLUE)
            .pattern(PATTERN_POLYLINE_ALPHA)
            .endCap(RoundCap()).width(POLYLINE_WIDTH)
    )
}
