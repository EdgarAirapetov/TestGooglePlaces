package com.edgarairapetov.testgoogleplaces.data.api.wrapper.direction

import com.google.gson.annotations.SerializedName

class DirectionResponse {
    @SerializedName("routes")
    var routes: List<DirectionRoute>? = null
}