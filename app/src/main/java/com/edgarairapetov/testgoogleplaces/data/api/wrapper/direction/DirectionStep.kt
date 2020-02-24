package com.edgarairapetov.testgoogleplaces.data.api.wrapper.direction

import com.google.gson.annotations.SerializedName

class DirectionStep {
    @SerializedName("polyline")
    var polyline: DirectionPolyline? = null
}