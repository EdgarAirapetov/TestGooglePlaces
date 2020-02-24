package com.edgarairapetov.testgoogleplaces.data.api.wrapper.direction

import com.google.gson.annotations.SerializedName

class DirectionRoute {
    @SerializedName("legs")
    var legs: List<DirectionLeg>? = null
}