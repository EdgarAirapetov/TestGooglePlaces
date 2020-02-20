package com.edgarairapetov.testgoogleplaces.api.model.route

import com.google.gson.annotations.SerializedName

class Step {
    @SerializedName("polyline")
    var polyline: Polyline? = null
}