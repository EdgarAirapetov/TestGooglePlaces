package com.edgarairapetov.testgoogleplaces.api.model.route

import com.google.gson.annotations.SerializedName

class Route {
    @SerializedName("legs")
    var legs: List<Leg>? = null
}