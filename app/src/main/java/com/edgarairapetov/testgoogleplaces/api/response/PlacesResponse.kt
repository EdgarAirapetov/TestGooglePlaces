package com.edgarairapetov.testgoogleplaces.api.response

import com.edgarairapetov.testgoogleplaces.api.model.place.Result
import com.google.gson.annotations.SerializedName

class PlacesResponse {
    @SerializedName("results")
    val results: ArrayList<Result>? = null

    var type: String? = null
}